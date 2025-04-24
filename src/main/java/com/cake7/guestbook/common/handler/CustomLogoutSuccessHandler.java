package com.cake7.guestbook.common.handler;

import com.cake7.guestbook.mapper.RefreshTokenMapper;
import com.cake7.guestbook.mapper.UserMapper;
import com.cake7.guestbook.common.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RefreshTokenMapper refreshTokenMapper;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final static Logger logger = LogManager.getLogger(CustomLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        try {
            String token = jwtUtils.extractToken(request);
            if (token != null && !token.isEmpty()) {
                // 토큰이 유효한 경우
                if (jwtUtils.validateToken(token)) {
                    String providerId = jwtUtils.parseToken(token).getSubject();
                    String userId = userMapper.findByProviderId(providerId);

                    // 해당 사용자의 모든 리프레시 토큰 무효화
                    if (userId != null) {
                        refreshTokenMapper.invalidateAllUserTokens(userId);
                        logger.info("All refresh tokens invalidated for user: {}", userId);
                    }
                }
            }

            // 리디렉션 설정
            response.sendRedirect("/");
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage());
            response.sendRedirect("/?logout-error=true");
        }
    }
}
