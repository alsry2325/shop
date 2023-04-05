package com.shop.config;


import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    MemberService memberService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()

                .loginPage("/members/login") //로그인페이지 url 설정 
                .defaultSuccessUrl("/") //로그인 성공시 이동할 url을 설정
                .usernameParameter("email") //로그인시 사용할 파라미터 이름을 email로 지정
                .failureUrl("/members/login/error") //로그인 실패시  이동할 url설정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 url 설정
                .logoutSuccessUrl("/") //로그아웃 성공시 이동할 url을 설정
        ;
        //http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        http.authorizeRequests()
                .mvcMatchers("/","/members/**","/item/**","/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                ;


                http.exceptionHandling()
                        .authenticationEntryPoint
                                (new CustomAuthenticationEntryPoint())
                        ;


    }

    @Override
    public void configure(WebSecurity web) throws Exception{

            web.ignoring().antMatchers("/css/**","/js/**","/img/**");
    }

    /*
       패스워드 암호화
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
             throws Exception {
            //사용자 아이디 / 비밀번호 인증을 처리하는 곳  -> 유효한 인증인지 확인
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }
}
