package com.cake7.guestbook.common.filter;

import com.cake7.guestbook.common.util.JwtUtils;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@NonNullApi
@Component
@RequiredArgsConstructor
public class PreventDuplicateLoginFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/oauth2/authorization/") ||
                requestURI.startsWith("/login/oauth2/code/")) {

            // 이미 인증된 사용자인지 확인
            String token = jwtUtils.extractToken(request);
            if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                // 이미 인증된 경우 홈페이지로 리다이렉션
                response.sendRedirect("/?error=already_authenticated");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
