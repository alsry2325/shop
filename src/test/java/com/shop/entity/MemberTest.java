package com.shop.entity;

import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;


    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "민교", roles = "USER")
    public void auditingTest(){
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();


        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityExistsException::new);

        System.out.println("register time: "+ member.getRegTime());
        System.out.println("update time: "+ member.getUpdateTime());
        System.out.println("create time: "+ member.getCreatedBy());
        System.out.println("modify time: "+ member.getModifiedBy());

    }

}
