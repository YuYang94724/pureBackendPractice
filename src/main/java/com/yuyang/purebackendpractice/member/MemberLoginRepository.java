package com.yuyang.purebackendpractice.member;

import com.yuyang.purebackendpractice.member.data.po.MemberLoginDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLoginRepository extends MongoRepository<MemberLoginDocument, String> {

    Page<MemberLoginDocument> findAllByUsernameOrderByCreatedByDesc(String username, Pageable pageable);
}
