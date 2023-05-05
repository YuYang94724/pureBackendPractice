package com.yuyang.purebackendpractice.wallet;

import com.yuyang.purebackendpractice.wallet.data.dto.WalletOpResponseDTO;
import com.yuyang.purebackendpractice.wallet.data.dto.WalletRequestDTO;
import com.yuyang.purebackendpractice.wallet.data.dto.WalletResponseDTO;
import com.yuyang.purebackendpractice.wallet.data.dto.WalletTransferRequestDTO;
import com.yuyang.purebackendpractice.wallet.data.vo.WalletVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1.0")
@Tag(name = "錢包")
@SecurityRequirement(name = "Authorization")
public class WalletController {

    @Resource
    private WalletService walletService;


    @GetMapping("/wallets")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "查詢餘額")
    WalletResponseDTO getWallets(Principal principal) {
        return walletService.query(principal.getName());
    }

    @PostMapping("/wallets:deposit")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "存錢")
    WalletOpResponseDTO deposit(@RequestBody WalletRequestDTO walletRequestDTO,
                                Principal principal) {
        return walletService.deposit(principal.getName(), walletRequestDTO.getAmount());
    }

    @PostMapping("/wallets:withdraw")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "提款")
    WalletOpResponseDTO withdraw(@RequestBody WalletRequestDTO walletRequestDTO,
                                Principal principal) {

        return walletService.withdraw(principal.getName(), walletRequestDTO.getAmount());
    }

    @Operation(summary = "查詢交易紀錄")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/wallets/transactions")
    Page<WalletVO> getWalletsTransactions(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            Principal principal) {
        return walletService.queryTransactions(principal.getName(), page, size);
    }

    @Operation(summary = "轉帳")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/wallets:transfer")
    WalletOpResponseDTO transfer(
            @RequestBody WalletTransferRequestDTO walletTransferRequestDTO,
            Principal principal) {
        return walletService
                .transfer(
                        principal.getName(),
                        walletTransferRequestDTO.getToMemberId(),
                        walletTransferRequestDTO.getAmount()
                );
    }
}
