package com.cake7.guestbook.handler;

import com.cake7.guestbook.service.JwtServiceImpl;
import com.cake7.guestbook.service.RefreshTokenServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtServiceImpl jwtServiceImpl;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final static Logger logger = LogManager.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException {
        try {

            String token = jwtServiceImpl.generateAccessToken(authentication);
            String refreshToken = refreshTokenServiceImpl.createRefreshToken(authentication);

//            response.setHeader("Authorization", "Bearer " + token);


            ResponseCookie cookie = ResponseCookie.from("jwt_token", token)
                                            .sameSite("strict")
                                            .httpOnly(true)
                                            .secure(true)
                                            .path("/")
                                            .maxAge(60 * 60)
                                            .build();

            response.addHeader("Set-Cookie", cookie.toString());

//            clearAuthenticationAttributes(request); // 이게 중요합니다!

            String targetUrl = UriComponentsBuilder.fromUriString("/").build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (Exception e) {
            logger.error("during onAuthenticationSuccess Exception error {}", e.getMessage());
            throw new ServletException("during onAuthenticationSuccess Exception error", e);
        }
    }
}
