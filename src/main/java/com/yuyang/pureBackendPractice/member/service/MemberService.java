package com.yuyang.pureBackendPractice.member.service;

import com.yuyang.pureBackendPractice.member.data.dto.MemberLoginResponseDTO;
import com.yuyang.pureBackendPractice.member.data.po.MemberPO;
import org.springframework.data.domain.Page;

import java.util.Optional;

 public interface MemberService {

    Optional<MemberPO> query(String username);
    Optional<MemberPO> login(String username, String password);
    Optional<MemberPO> modifyAbout(String username, String about);
    Optional<MemberPO> changePassword(String username, String oldPwd, String newPwd);
    Optional<MemberPO> remove(String username);
    Page<MemberLoginResponseDTO> queryMembersRecords(String username, Integer page, Integer size);
}
