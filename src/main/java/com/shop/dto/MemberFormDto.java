package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {

    private String name;  //회원이름
    private String email;   //이메일
    private String password;    //비밀번호
    private String address; //주소

}
