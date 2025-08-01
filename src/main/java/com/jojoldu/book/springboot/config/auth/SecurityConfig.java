package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf 비활성화(아직 뭔지 모름)

        // heaers

        // authorizeRequests - URL별로 권한 관리를 설정하는 옵션 시작점

        // requestMatchers
        // authorizeRequests가 있어야 옵션을 사용할 수 있다.
        // "/css/**" 이렇게 지정된 URL들을 permitAll(전체 열람 권한) hasRole(특정 인원 권한)로 권한을 관리한다

        // anyRequest - 설정된 값 이외의 URL들

        // logout().ogoutSuccessUrl - 로그아웃시에 어느지점으로 갈 것인지 URL설정

        // oauth2Login - 로그인 기능에 대한 여러 설정의 진입점
        // userInfoEndpoint - 로그인 성공 이후 사용자 정보를 가져올 때의 설정
        // userService - 로그인 성공 시에 OAuth2UserService를 상속 받고 있는 구현체를 등록해서 사용자 정보를 어떻게 할지 정한다(?)
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 콘솔 사용을 위한 설정
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/WEB-INF/views/**").permitAll()
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                );
        return http.build();
    }
}

