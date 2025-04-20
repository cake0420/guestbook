package com.cake7.guestbook.filter;

import com.cake7.guestbook.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
            Claims claims = jwtService.parseToken(token);
            String email = claims.getSubject();
            String authorities = (String) claims.get("authorities");

            List<SimpleGrantedAuthority> grantedAuthorities = Arrays.stream(authorities.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities);

            //이 인증 객체를 시큐리티 컨텍스트에 등록하면, 이후 컨트롤러 등에서 @AuthenticationPrincipal을 통해 유저 정보를 가져올 수 있음.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //다음 필터 혹은 서블릿으로 요청을 넘김.
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
