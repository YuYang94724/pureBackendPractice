package com.yuyang.pureBackendPractice.member;

import com.yuyang.pureBackendPractice.member.data.po.MemberLoginDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLoginRepository extends MongoRepository<MemberLoginDocument, String> {

    Page<MemberLoginDocument> findAllByUsernameOrderByCreatedByDesc(String username, Pageable pageable);
}
