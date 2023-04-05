package com.shop.controller;


import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;  //테스트용

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     *  전회원 등록하는 메소드테스트
     * @param email
     * @param password
     * @return
     */
    public Member createMember(String email,String password){

        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("정민교");
        memberFormDto.setAddress("서울시 이태원동");
        memberFormDto.setPassword(password);

        Member member = Member.createMember(memberFormDto,passwordEncoder);
        return memberService.saveMember(member);
    }
    
    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception{

        String email = "test@test.com";
        String password = "1234";
        this.createMember(email,password);

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")  //userParameter를 이용하여 이메일을 세팅하여 로그인url에 요청
                .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }
    
    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{

        String emali = "test@email.com";
        String password = "1234";
        this.createMember(emali,password);

        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")  //userParameter를 이용하여 이메일을 세팅하여 로그인url에 요청
                        .user(emali).password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated()); // 입력한 비밀번호가 아닌 다른비밀번호로 로그인을 시도하여  인증되지않는 결과값이  출력되어 테스트통과
    }
}
