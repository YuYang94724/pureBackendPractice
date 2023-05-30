package com.yuyang.pureBackendPractice.member.data.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyang.pureBackendPractice.member.data.enu.MemberLoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@EntityListeners(AuditingEntityListener.class)
//這是用來給MongoDB使用的
public class MemberLoginDocument {

    @Id
    String id;

    @Indexed
    String username;

//    @Enumerated(value = EnumType.STRING)
    MemberLoginType type;

    @CreatedBy
    String createdBy;

    @JsonFormat(pattern = "yyyyMMdd")
    @CreatedDate
    LocalDate createdDate;

    @LastModifiedBy
    String lastModifiedBy;

    @JsonFormat(pattern = "yyyyMMdd")
    @LastModifiedDate
    LocalDate lastModifiedDate;
}
