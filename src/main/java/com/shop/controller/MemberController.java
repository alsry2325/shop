package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


//http://localhost/members
@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private  final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 뷰
     * @param model
     * @return
     */
    @GetMapping(value = "/new")
    public String memberForm(Model model) {

        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    /**
     * 회원가입 API
     * @param memberFormDto
     * @return
     */
    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,Model model){


        if (bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try{
            Member member = Member.createMember(memberFormDto,passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            //회원가입시 중복 회원 가입 예외가 발생하면 에러메세지를 뷰로 전달
            model.addAttribute("errorMessage",e.getMessage());
            return "member/memberForm";
        }


        //메인화면으로 이동
        return "redirect:/";
    }

    /**
     * 로그인 뷰
     * @return
     */
    @GetMapping(value = "/login")
    public String loginMeber(){
        return "/member/memberLoginForm";
    }

    /**
     * 로그인 에러메세지
     * @param model
     * @return
     */
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주새요");
        return "/member/memberLoginForm";
    }

}
