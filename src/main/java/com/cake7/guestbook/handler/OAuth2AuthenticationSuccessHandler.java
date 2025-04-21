package com.cake7.guestbook.handler;

import com.cake7.guestbook.service.JwtServiceImpl;
import com.cake7.guestbook.service.RefreshTokenServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtServiceImpl jwtServiceImpl;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final static Logger logger = LogManager.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            String token = jwtServiceImpl.generateAccessToken(authentication);
            String refreshToken = refreshTokenServiceImpl.createRefreshToken(authentication);

//            response.setHeader("Authorization", "Bearer " + token);

            Cookie cookie = new Cookie("jwt_token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);

            String targetUrl = UriComponentsBuilder.fromUriString("/").build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (ServletException e) {
            logger.error("during onAuthenticationSuccess ServletException error {}", e.getMessage());
            throw new ServletException("during onAuthenticationSuccess ServletException error", e);
        } catch (IOException e) {
            logger.error("during onAuthenticationSuccess IOException error {}", e.getMessage());
            throw new IOException("during onAuthenticationSuccess IOException error", e);
        } catch (Exception e) {
            logger.error("during onAuthenticationSuccess Exception error {}", e.getMessage());
            throw new ServletException("during onAuthenticationSuccess Exception error", e);
        }
    }
}
