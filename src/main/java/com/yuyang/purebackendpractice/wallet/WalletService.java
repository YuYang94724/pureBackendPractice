package com.yuyang.purebackendpractice.wallet;

import com.yuyang.purebackendpractice.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final MemberRepository memberRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletService(MemberRepository memberRepository, WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.memberRepository = memberRepository;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }
}
