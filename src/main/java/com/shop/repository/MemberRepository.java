package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //유저이메일찾기
    Member findByEmail(String email);
}
