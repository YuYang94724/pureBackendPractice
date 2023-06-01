package com.yuyang.pureBackendPractice.wallet.service.impl;

import com.yuyang.pureBackendPractice.member.repository.MemberRepository;
import com.yuyang.pureBackendPractice.wallet.data.dto.WalletOpResponseDTO;
import com.yuyang.pureBackendPractice.wallet.data.dto.WalletResponseDTO;
import com.yuyang.pureBackendPractice.wallet.data.enu.WalletOperationType;
import com.yuyang.pureBackendPractice.wallet.data.po.WalletTransactionPO;
import com.yuyang.pureBackendPractice.wallet.data.vo.WalletVO;
import com.yuyang.pureBackendPractice.wallet.repository.WalletRepository;
import com.yuyang.pureBackendPractice.wallet.repository.WalletTransactionRepository;
import com.yuyang.pureBackendPractice.wallet.service.WalletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final MemberRepository memberRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletServiceImpl(MemberRepository memberRepository, WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.memberRepository = memberRepository;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    public WalletResponseDTO query(String name){
        return walletRepository.findByMemberId(memberRepository.findByUsername(name).get().getId())
                .map(v -> WalletResponseDTO.builder()
                    .memberId(v.getMemberId())
                    .balance(v.getBalance())
                    .username(name)
                    .build())
                .orElseThrow(() -> new RuntimeException("WalletService query Err"));
    }

    @Transactional(rollbackFor = {Throwable.class})
//    @javax.transaction.Transactional(rollbackOn = {RuntimeException.class})
    public WalletOpResponseDTO deposit(String name, BigDecimal amount){
        return memberRepository
                .findByUsername(name)
                .flatMap(v ->
                    walletRepository
                            .findByMemberId(v.getId())
                            .map(w ->{
                                BigDecimal beforeBalance = w.getBalance();
                                BigDecimal afterBalance = w.getBalance().add(amount);
                                w.setBalance(afterBalance);
                                walletRepository.save(w);
                                return walletTransactionRepository.save(WalletTransactionPO
                                        .builder()
                                        .afterBalance(afterBalance)
                                        .beforeBalance(beforeBalance)
                                        .operationType(WalletOperationType.DEPOSIT)
                                        .walletId(w.getId())
                                        .amount(amount)
                                        .operationUuid(UUID.randomUUID()+"")
                                        .memo(WalletOperationType.DEPOSIT.name() + "to " + w.getMemberId())
                                        .id(null)
                                        .build()
                                );
                            }))
                .map(v -> WalletOpResponseDTO
                        .builder()
                        .fromMemberId(v.getWalletId())
                        .fromMemberBalance(v.getBeforeBalance())
                        .toMemberId(v.getWalletId())
                        .toMemberBalance(v.getAfterBalance())
                        .build()
                )
                .orElseThrow(() -> new RuntimeException("WalletService deposit Err"));
    }
    @Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public WalletOpResponseDTO withdraw(String name, BigDecimal amount){
        return memberRepository.findByUsername(name)
                .flatMap(v -> walletRepository.findByMemberId(v.getId())
                        .map(w -> {
                            BigDecimal beforeBalance = w.getBalance();
                            BigDecimal afterBalance = w.getBalance().subtract(amount);
                            w.setBalance(afterBalance);
                            walletRepository.save(w);
                            return walletTransactionRepository.save(WalletTransactionPO
                                    .builder()
                                    .afterBalance(afterBalance)
                                    .beforeBalance(beforeBalance)
                                    .operationType(WalletOperationType.WITHDRAW)
                                    .walletId(w.getId())
                                    .amount(amount).build()
                            );
                        })
                )
                .map(v -> WalletOpResponseDTO
                        .builder()
                        .fromMemberId(v.getWalletId())
                        .fromMemberBalance(v.getBeforeBalance())
                        .toMemberId(v.getWalletId())
                        .toMemberBalance(v.getAfterBalance())
                        .build()
                )
                .orElseThrow(() -> new RuntimeException("WalletService withdraw Err"));
    }
    @Transactional(rollbackFor = {Exception.class})
    public Page<WalletVO> queryTransactions(String name, int page, int size){
        return memberRepository
                .findByUsername(name)
                .map(v -> walletTransactionRepository.findAllTxRecords(v.getId(), PageRequest.of(page, size)))
                .orElseThrow( () -> new RuntimeException("WalletService queryTransactions Err"));

    }

    public WalletOpResponseDTO transfer(String name, Long toId, BigDecimal amount){
        return memberRepository
                .findByUsername(name)
                .flatMap(v -> walletRepository
                        .findByMemberId(v.getId())
                        .flatMap(w1 -> walletRepository
                                    .findByMemberId(toId)
                                    .map(w2 -> {
                                        BigDecimal  beforeBalance = w1.getBalance();
                                        BigDecimal afterBalance = w1.getBalance().subtract(amount);
                                        w1.setBalance(afterBalance);
                                        walletRepository.save(w1);

                                        BigDecimal  beforeBalance2 = w2.getBalance();
                                        BigDecimal afterBalance2 = w2.getBalance().add(amount);
                                        w2.setBalance(afterBalance2);
                                        walletRepository.save(w2);

                                        final WalletTransactionPO wt1 = walletTransactionRepository.save(WalletTransactionPO
                                                .builder()
                                                .id(null)
                                                .walletId(w1.getId())
                                                .operationType(WalletOperationType.TRANSFER_OUT)
                                                .amount(amount)
                                                .beforeBalance(beforeBalance)
                                                .afterBalance(afterBalance)
                                                .operationUuid(UUID.randomUUID() + "")
                                                .build());
                                        final WalletTransactionPO wt2 = walletTransactionRepository.save(WalletTransactionPO
                                                .builder()
                                                .id(null)
                                                .walletId(w2.getId())
                                                .operationType(WalletOperationType.TRANSFER_IN)
                                                .amount(amount)
                                                .beforeBalance(beforeBalance2)
                                                .afterBalance(afterBalance2)
                                                .operationUuid(UUID.randomUUID() + "")
                                                .build());

                                        return Pair.of(wt1, wt2);
                                    }))

                )
                .map(v -> WalletOpResponseDTO
                        .builder()
                        .fromMemberId(v.getFirst().getWalletId())
                        .toMemberId(v.getSecond().getWalletId())
                        .fromMemberBalance(v.getFirst().getAfterBalance())
                        .toMemberBalance(v.getSecond().getAfterBalance())
                        .build()
                )
                .orElseThrow(() -> new RuntimeException("WalletService transfer Err"));

    }

}
