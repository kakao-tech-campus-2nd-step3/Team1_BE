package com.example.team1_be.util.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtToken jwtToken;
    private static final String URI = "/auth/success";

    @Autowired
    public OAuth2SuccessHandler(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        // 사용자 정보를 바탕으로 JWT 생성
        String token = jwtToken.createToken(authentication);

        // 토큰 전달을 위한 redirect
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
            .queryParam("accessToken", token)
            .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
