package com.shop.config;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    /**
     * 인증되지 않는 사용자가 ajax로 리소스를 요청하는 경우 Unauthorized 에러를 발생시키고 나머지경우는 
     * 로그인페이지로 리다이렉트 시킴
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))){

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
        }else {
            response.sendRedirect("/members/login");
        }
    }
}
