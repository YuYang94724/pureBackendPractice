package com.yuyang.pureBackendPractice.member.data.dto;

import com.yuyang.pureBackendPractice.member.data.enu.MemberLoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDTO {

    String username;

    MemberLoginType type;

    String createdBy;

    String createdDateStr;
}
