package team1.BE.seamless.util.errorException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class SecurityEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public SecurityEntryPoint(
        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) {
        resolver.resolveException(request, response, null, authException);
    }
}
