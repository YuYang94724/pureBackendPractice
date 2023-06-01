package com.yuyang.pureBackendPractice.wallet.data.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyang.pureBackendPractice.wallet.data.enu.WalletOperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallet_transaction")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class WalletTransactionPO implements Serializable {

    private static final long serialVersionUID = -5376084452489538615L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;
    @Column
    Long walletId;
    @Column
    String operationUuid;
    @Column
    @Enumerated(value = EnumType.STRING)
    WalletOperationType operationType;
    @Column
    BigDecimal beforeBalance;
    @Column
    BigDecimal amount;
    @Column
    BigDecimal afterBalance;
    @Column
    String memo;
    @Column
    @CreatedBy
    String createdBy;
    @JsonFormat(pattern = "yyyyMMdd")
    @Column
    @CreatedDate
    LocalDate createdDate;
    @Column
    @LastModifiedBy
    String lastModifiedBy;
    @JsonFormat(pattern = "yyyyMMdd")
    @Column
    @LastModifiedDate
    LocalDate lastModifiedDate;
}
