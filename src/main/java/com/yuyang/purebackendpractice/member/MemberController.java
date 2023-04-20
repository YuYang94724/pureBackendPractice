package com.yuyang.purebackendpractice.member;

import com.yuyang.purebackendpractice.comfig.securtiy.JwtTokenService;
import com.yuyang.purebackendpractice.member.data.dto.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Tag(name = "Member")
@SecurityRequirement(name = "Authentication")
@RestController
@RequestMapping("api/v1.0")
public class MemberController {

    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;

    public MemberController(JwtTokenService jwtTokenService, MemberService memberService) {
        this.jwtTokenService = jwtTokenService;
        this.memberService = memberService;
    }

//
//    @Operation(summary = "查詢用戶資料")
//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/members")
//    MemberResponseDTO getMembers(Principal principal) {
//        return memberService
//                .query(principal.getName())
//                .map(v -> MemberResponseDTO
//                        .builder()
//                        .id(v.getId())
//                        .username(v.getUsername())
//                        .about(v.getAbout())
//                        .avatarLink(v.getAvatarLink())
//                        .role(v.getRole())
//                        .createdDateStr(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedDate()), ZoneOffset.ofHours(8))))
//                        .build()
//                )
//                .orElseThrow(() -> new RuntimeException("getMembers Err"));
//    }
}
