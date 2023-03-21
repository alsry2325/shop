package com.shop.service;


import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
        회원가입 저장
     */
    public Member saveMember(Member member) {
        validateDuplicateMember(member);

        return memberRepository.save(member);
    }
    /*
        이메일아이디중복검색
     */
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다");


        }
    }

}
