package com.yuyang.pureBackendPractice.member.service.impl;

import com.yuyang.pureBackendPractice.member.data.dto.MemberLoginResponseDTO;
import com.yuyang.pureBackendPractice.member.data.enu.MemberLoginType;
import com.yuyang.pureBackendPractice.member.data.enu.MemberRole;
import com.yuyang.pureBackendPractice.member.data.enu.MemberStatus;
import com.yuyang.pureBackendPractice.member.data.po.MemberLoginDocument;
import com.yuyang.pureBackendPractice.member.data.po.MemberPO;
import com.yuyang.pureBackendPractice.member.repository.MemberLoginRepository;
import com.yuyang.pureBackendPractice.member.repository.MemberRepository;
import com.yuyang.pureBackendPractice.member.service.MemberService;
import com.yuyang.pureBackendPractice.wallet.data.enu.WalletStatus;
import com.yuyang.pureBackendPractice.wallet.data.po.WalletPO;
import com.yuyang.pureBackendPractice.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
//
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final WalletRepository walletRepository;
    private final MemberLoginRepository memberLoginRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final RestTemplate restTemplate;

    @Value("${custom.avatar.uri}")
    private String avatarUri;

    public MemberServiceImpl(PasswordEncoder passwordEncoder, WalletRepository walletRepository, AuthenticationManager authenticationManager, MemberRepository memberRepository, MemberLoginRepository memberLoginRepository, StringRedisTemplate stringRedisTemplate, RestTemplate restTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.memberRepository = memberRepository;
        this.walletRepository = walletRepository;
        this.memberLoginRepository = memberLoginRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void fetchRandomAvatarLink() {
        Optional.ofNullable(restTemplate.getForEntity(avatarUri, String.class).getBody())
                .map(v -> stringRedisTemplate.opsForSet().add("randomAvatarLinks", v));
    }

    @Cacheable(cacheNames = {"members"}, key = "#username")
    public Optional<MemberPO> query(String username) {
        return memberRepository.findByUsername(username);
    }


    @CachePut(cacheNames = {"members"}, key = "#result.username")
    public Optional<MemberPO> login(String username, String password){
       return memberRepository.findByUsername(username).map(v -> {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            memberLoginRepository.save(MemberLoginDocument
                    .builder()
                    .id(null)
                    .username(username)
                    .type(MemberLoginType.LOGIN).build());
            return v;
        }).or(()->{
           String randomAvatarLink = stringRedisTemplate.opsForSet().randomMember("randomAvatarLinks");
           if (randomAvatarLink == null || randomAvatarLink.isBlank()) {
               randomAvatarLink = restTemplate.getForEntity(avatarUri, String.class).getBody();
           }
           final MemberPO memberPO = memberRepository.save(
                   MemberPO
                           .builder()
                           .username(username)
                           .password(passwordEncoder.encode(password))
                           .avatarLink(randomAvatarLink)
                           .about("")
                           .role(username.equals("admin") ? MemberRole.ROLE_ADMIN : MemberRole.ROLE_USER)
                           .status(MemberStatus.ACTIVE)
                           .build());
           walletRepository.save(
                   WalletPO
                           .builder()
                           .memberId(memberPO.getId())
                           .balance(BigDecimal.ZERO)
                           .status(WalletStatus.ACTIVE)
                           .build());
           memberLoginRepository.save(
                   MemberLoginDocument
                           .builder()
                           .username(username)
                           .type(MemberLoginType.SIGNUP)
                           .build()
           );

           return Optional.of(memberPO);
       });
    }

    @CachePut(cacheNames = {"members"}, key = "#result.username")
    public Optional<MemberPO> modifyAbout(String username, String about){

        return memberRepository.findByUsername(username)
                .map(v -> {
                    v.setAbout(about);
                    return memberRepository.save(v);
                });
    }

    public Optional<MemberPO> changePassword(String username, String oldPwd, String newPwd){

        return memberRepository.findByUsername(username)
                .filter(v -> passwordEncoder.matches(oldPwd, v.getPassword()))
                .map( v -> {
                    v.setPassword(passwordEncoder.encode(newPwd));
                    return memberRepository.save(v);
                });
    }

    public Optional<MemberPO> remove(String username){
        return memberRepository.findByUsername(username)
                .flatMap(v ->{
                    v.setStatus(MemberStatus.FREEZE);
                    final MemberPO memberPO = memberRepository.save(v);
                    return walletRepository.findByMemberId(memberPO.getId())
                            .map(w -> {
                                w.setStatus(WalletStatus.FREEZE);
                                walletRepository.save(w);
                                return v;
                            });
                });
    }

    public Page<MemberLoginResponseDTO> queryMembersRecords(String username, Integer page, Integer size) {
        return memberLoginRepository
                .findAllByUsernameOrderByCreatedByDesc(username, PageRequest.of(page, size))
                .map(v -> MemberLoginResponseDTO
                        .builder()
                        .username(v.getUsername())
                        .type(v.getType())
                        .createdBy(v.getCreatedBy())
                        .createdDateStr(v.getCreatedDate()+"")
                        .build());
    }
}
