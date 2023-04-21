package com.yuyang.purebackendpractice.wallet.data.po;

import com.yuyang.purebackendpractice.wallet.data.enu.WalletStatus;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    Long memberId;

    BigDecimal balance;

    @Enumerated(value = EnumType.STRING)
    WalletStatus status;

    @CreatedBy
    String createdBy;

    @CreatedDate
    Long createdDate;

    @LastModifiedBy
    String lastModifiedBy;

    @LastModifiedDate
    Long lastModifiedDate;
}
