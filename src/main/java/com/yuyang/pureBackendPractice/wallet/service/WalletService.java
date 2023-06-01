package com.yuyang.pureBackendPractice.wallet.service;

import com.yuyang.pureBackendPractice.wallet.data.dto.WalletOpResponseDTO;
import com.yuyang.pureBackendPractice.wallet.data.dto.WalletResponseDTO;
import com.yuyang.pureBackendPractice.wallet.data.vo.WalletVO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface WalletService {

    WalletResponseDTO query(String name);
    WalletOpResponseDTO deposit(String name, BigDecimal amount);
    WalletOpResponseDTO withdraw(String name, BigDecimal amount);
    Page<WalletVO> queryTransactions(String name, int page, int size);
    WalletOpResponseDTO transfer(String name, Long toId, BigDecimal amount);
}
