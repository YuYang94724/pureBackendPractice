package com.yuyang.pureBackendPractice.member.repository;

import com.yuyang.pureBackendPractice.member.data.po.MemberPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberPO, Long>, JpaSpecificationExecutor<MemberPO> {

    Optional<MemberPO> findByUsername(String username);
}

