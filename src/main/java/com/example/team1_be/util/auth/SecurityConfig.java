package com.example.team1_be.util.auth;

import com.example.team1_be.service.AuthService;
import com.example.team1_be.util.fiter.TokenAuthenticationFilter;
import com.example.team1_be.util.fiter.TokenExceptionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;
    private final OAuth2SuccessHandler successHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final TokenExceptionFilter tokenExceptionFilter;

    @Autowired
    public SecurityConfig(AuthService authService, OAuth2SuccessHandler successHandler,
        TokenAuthenticationFilter tokenAuthenticationFilter,
        TokenExceptionFilter tokenExceptionFilter) {
        this.authService = authService;
        this.successHandler = successHandler;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.tokenExceptionFilter = tokenExceptionFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .headers(c -> c.frameOptions().disable())
            .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(request -> request
//                swagger
                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs",
                    "/api-docs/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/login/**", "/auth/**", "/oauth2/**")
                .permitAll()
//                확장자
                .requestMatchers("/", "/error", "/favicon.ico", "/**/*.png", "/**/*.gif",
                    "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll()
//                인증, h2
                .requestMatchers("/auth/**", "/h2-console/**").permitAll()
                .anyRequest()
                .authenticated()
            )

            .oauth2Login(oauth -> oauth
                .userInfoEndpoint(c -> c.userService(authService))
                .successHandler(successHandler)
            )

            .addFilterAfter(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(tokenExceptionFilter, tokenAuthenticationFilter.getClass());

        return http.build();
    }
}
