package com.cake7.guestbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다
@EnableMethodSecurity (securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers("/", "/index.html", "/v1/sign-in", "/v1/sign-up", "/swagger-ui/**", "/v3/api-docs/**").permitAll()  // 순서 중요: permitAll 먼저
                    .requestMatchers("/login/oauth2/code/google/**").permitAll()
                    .requestMatchers("/oauth2/authorization/google/**").permitAll()
                    .requestMatchers("/v1/user/**").hasRole("USER")
                    .requestMatchers("/v1/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
//                        .loginPage()
                        .defaultSuccessUrl("/", true)
                )
                .formLogin(form -> form
                        .loginPage("/")  // 커스텀 로그인 페이지 URL 설정
                        .loginProcessingUrl("/login")  // 로그인 요청 URL
                        .defaultSuccessUrl("/", true)  // 로그인 성공 후 리디렉션할 페이지 설정
                        .failureUrl("/?error=true")  // 로그인 실패 시 리디렉션할 페이지 설정
                        .permitAll()  // 로그인 페이지는 누구나 접근 가능
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_DB\n" +
                "ROLE_DB > ROLE_USER\n" +
                "ROLE_USER > ROLE_ANONYMOUS");
    }


}
