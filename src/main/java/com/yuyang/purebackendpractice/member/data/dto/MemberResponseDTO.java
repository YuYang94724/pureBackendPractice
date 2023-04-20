package com.yuyang.purebackendpractice.member.data.dto;

import com.yuyang.purebackendpractice.member.data.enu.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {

    Long id;

    String username;

    String about;

    String avatarLink;

    MemberRole role;

    String createdDateStr;
}
