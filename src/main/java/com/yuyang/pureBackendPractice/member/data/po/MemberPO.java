package com.yuyang.pureBackendPractice.member.data.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyang.pureBackendPractice.member.data.enu.MemberRole;
import com.yuyang.pureBackendPractice.member.data.enu.MemberStatus;
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
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MemberPO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column(name = "user_name", unique = true)
    String username;
    @Column
    String password;
    @Column
    String about;
    @Column
    String avatarLink;

    // Enumerated用來指定對應數據庫的資料類型
    // EnumType.STRING比較推薦使用， EnumType.ORDINAL是返回該index(從0開始)的值
    @Enumerated(value = EnumType.STRING)
    @Column
    MemberRole role;

    @Enumerated(value = EnumType.STRING)
    @Column
    MemberStatus status;
    //下方為使用配置的方式
    @CreatedBy
    @Column
    String createdBy;

    @JsonFormat(pattern = "yyyyMMdd")
    @CreatedDate
    @Column
    LocalDate createdDate;

    @LastModifiedBy
    @Column
    String lastModifiedBy;

    @JsonFormat(pattern = "yyyyMMdd")
    @LastModifiedDate
    @Column
    LocalDate lastModifiedDate;
}
