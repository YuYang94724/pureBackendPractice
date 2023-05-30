package com.yuyang.pureBackendPractice.wallet.data.vo;

import com.yuyang.pureBackendPractice.wallet.data.enu.WalletOperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletVO {

    Long id;

    String username;

    String operationUuid;

    WalletOperationType operationType;

    BigDecimal beforeBalance;

    BigDecimal amount;

    BigDecimal afterBalance;

    String createBy;

    LocalDate createDate;

    String lastModifiedBy;

    LocalDate lastModifiedDate;
}
