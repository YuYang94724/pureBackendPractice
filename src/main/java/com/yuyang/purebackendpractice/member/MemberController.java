package com.yuyang.purebackendpractice.member;

import com.yuyang.purebackendpractice.comfig.securtiy.JwtTokenService;
import com.yuyang.purebackendpractice.member.data.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Tag(name = "Member")
@SecurityRequirement(name = "Authentication")
@RestController
@RequestMapping("api/v1.0")
@Slf4j
public class MemberController {

    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;

    public MemberController(JwtTokenService jwtTokenService, MemberService memberService) {
        this.jwtTokenService = jwtTokenService;
        this.memberService = memberService;
    }

    @Operation(summary = "登入")
    @PostMapping("/members:login")
    ResponseEntity<String> postMembersLogin(@RequestBody LoginRequestDto loginRequestDto) {
        System.err.println("postMembersLogin username = {}, password = {} " + loginRequestDto.getUsername() + loginRequestDto.getPassword());
        return memberService
                .login(loginRequestDto.getUsername(), loginRequestDto.getPassword())
                .map(v -> jwtTokenService.generate(v.getUsername(), 60L * 60L * 1000L, UUID.randomUUID().toString()))
                .map(v -> ResponseEntity
                        .status(HttpStatus.OK)
                        .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", v))
                        .body(v)
                )
                .orElseThrow(() -> new RuntimeException("postMembersLogin Err"));
    }

    @Operation(summary = "查詢用戶資料")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/members")
    MemberResponseDTO getMembers(Principal principal) {
        return memberService
                .query(principal.getName())
                .map(v -> MemberResponseDTO
                        .builder()
                        .id(v.getId())
                        .username(v.getUsername())
                        .about(v.getAbout())
                        .avatarLink(v.getAvatarLink())
                        .role(v.getRole())
                        .createdDateStr(v.getCreatedDate()+"")
                        .build()
                )
                .orElseThrow(() -> new RuntimeException("getMembers Err"));
    }

    @Operation(summary = "修改自介")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/members:modifyAbout")
    ResponseEntity<Object> patchMembersAbout(
            @RequestBody MemberAboutRequestDTO memberAboutRequestDTO,
            Principal principal) {
        return memberService
                .modifyAbout(principal.getName(), memberAboutRequestDTO.getAbout())
                .map(v -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new RuntimeException("patchMembersAbout Err"));
    }

    @Operation(summary = "修改密碼")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/members:modifyPassword")
    ResponseEntity<Object> patchMembersPassword(
            @RequestBody MemberPasswordRequestDTO memberPasswordRequestDTO,
            Principal principal) {
        return memberService
                .changePassword(principal.getName(), memberPasswordRequestDTO.getOldPassword(), memberPasswordRequestDTO.getNewPassword())
                .map(v -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new RuntimeException("patchMembersPassword Err"));
    }

    @Operation(summary = "凍結用戶")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/members/" + "{username}")
    ResponseEntity<Object> removeMember(@PathVariable(name = "username") String username){
        System.err.println("removeMember進來了");
        System.err.println(username);
        return memberService
                .remove(username)
                .map(v -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new RuntimeException("removeMember Err"));
    }

    @Operation(summary = "登入記錄")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/members/records")
    Page<MemberLoginResponseDTO> getMembersRecords(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            Principal principal) {
        return memberService.queryMembersRecords(principal.getName(), page, size);
    }
}
