package team1.be.seamless.util.fiter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import team1.be.seamless.util.auth.JwtToken;
import team1.be.seamless.util.errorException.CustomExceptionHandler;
import team1.be.seamless.util.errorException.RuntimeHandler;
import team1.be.seamless.util.errorException.StatusResponse;
import team1.be.seamless.util.page.SingleResult;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtToken jwtToken;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    public TokenAuthenticationFilter(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(7);
            try {
                jwtToken.validateToken(token);
                setAuthentication(token);
            } catch (RuntimeHandler e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"errorCode\": 401, \"errorMessage\": \"" + e.getMessage() + "\"}");

                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtToken.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
