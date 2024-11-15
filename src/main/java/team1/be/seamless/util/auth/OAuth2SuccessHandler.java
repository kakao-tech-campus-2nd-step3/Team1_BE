package team1.be.seamless.util.auth;

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
    private static final String URI = "/api/auth/success";

    @Autowired
    public OAuth2SuccessHandler(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String token = jwtToken.createToken(authentication);


        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("accessToken", token)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
