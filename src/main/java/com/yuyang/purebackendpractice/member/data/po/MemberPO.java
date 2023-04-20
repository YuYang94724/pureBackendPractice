package com.yuyang.purebackendpractice.member.data.po;

import com.yuyang.purebackendpractice.member.data.enu.MemberRole;
import com.yuyang.purebackendpractice.member.data.enu.MemberStatus;
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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MemberPO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "user_name", unique = true)
    String username;

    String password;

    String about;

    String avatarLink;

    // EnumType.STRING比較推薦使用， EnumType.ORDINAL是返回該index(從0開始)的值
    @Enumerated(value = EnumType.STRING)
    MemberRole role;

    @Enumerated(value = EnumType.STRING)
    MemberStatus status;
    //下方為使用配置的方式
    @CreatedBy
    String createdBy;

    @CreatedDate
    Long createdDate;

    @LastModifiedBy
    String lastModifiedBy;

    @LastModifiedDate
    Long lastModifiedDate;
}
