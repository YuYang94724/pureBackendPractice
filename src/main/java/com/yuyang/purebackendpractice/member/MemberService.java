package com.yuyang.purebackendpractice.member;

import com.yuyang.purebackendpractice.member.data.dto.MemberLoginResponseDTO;
import com.yuyang.purebackendpractice.member.data.enu.MemberLoginType;
import com.yuyang.purebackendpractice.member.data.enu.MemberRole;
import com.yuyang.purebackendpractice.member.data.enu.MemberStatus;
import com.yuyang.purebackendpractice.member.data.po.MemberLoginDocument;
import com.yuyang.purebackendpractice.member.data.po.MemberPO;
import com.yuyang.purebackendpractice.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
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
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class MemberService {
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

    public MemberService(PasswordEncoder passwordEncoder, WalletRepository walletRepository, AuthenticationManager authenticationManager, MemberRepository memberRepository, MemberLoginRepository memberLoginRepository, StringRedisTemplate stringRedisTemplate, RestTemplate restTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.memberRepository = memberRepository;
        this.walletRepository = walletRepository;
        this.memberLoginRepository = memberLoginRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void fetchRandomAvatarLink() {
        Optional.ofNullable(restTemplate.getForEntity(avatarUri, String.class).getBody())
                .map(v -> stringRedisTemplate.opsForSet().add("randomAvatarLinks", v));
    }

}
