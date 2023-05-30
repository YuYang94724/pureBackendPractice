package com.yuyang.pureBackendPractice.wallet.data.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyang.pureBackendPractice.wallet.data.enu.WalletStatus;
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
@Table(name = "wallet")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class WalletPO implements Serializable {

    private static final long serialVersionUID = 6024047340357745034L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    Long memberId;

    @Column
    BigDecimal balance;

    @Column
    @Enumerated(value = EnumType.STRING)
    WalletStatus status;

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
