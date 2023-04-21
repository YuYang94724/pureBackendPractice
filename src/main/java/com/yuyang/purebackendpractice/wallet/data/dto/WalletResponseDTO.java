package com.yuyang.purebackendpractice.wallet.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseDTO {

    Long memberId;

    String username;

    BigDecimal balance;
}

