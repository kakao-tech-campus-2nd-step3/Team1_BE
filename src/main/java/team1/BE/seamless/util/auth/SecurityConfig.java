package team1.BE.seamless.util.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import team1.BE.seamless.service.AuthService;
import team1.BE.seamless.util.errorException.SecurityEntryPoint;
import team1.BE.seamless.util.fiter.TokenAuthenticationFilter;
import team1.BE.seamless.util.fiter.TokenExceptionFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;
    private final OAuth2SuccessHandler successHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final TokenExceptionFilter tokenExceptionFilter;
    private final SecurityEntryPoint SecurityException;
    private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;


    @Autowired
    public SecurityConfig(AuthService authService, OAuth2SuccessHandler successHandler,
        TokenAuthenticationFilter tokenAuthenticationFilter,
        TokenExceptionFilter tokenExceptionFilter,
        SecurityEntryPoint securityException,
        HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository) {
        this.authService = authService;
        this.successHandler = successHandler;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.tokenExceptionFilter = tokenExceptionFilter;
        SecurityException = securityException;
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
        HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) throws Exception {
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
                    .requestMatchers("/login/**", "/api/auth/**", "/oauth2/**")
                    .permitAll()
//                확장자
                    .requestMatchers("/", "/error", "/favicon.ico", "/**/*.png", "/**/*.gif",
                        "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js")
                    .permitAll()
//                인증, h2
                    .requestMatchers("/h2-console/**", "/auth/**","/api/test/**").permitAll()
//                멤버 생성
                    .requestMatchers(HttpMethod.POST,"/api/project/**/member/**").permitAll()
//                멤버 조회
                    .requestMatchers(HttpMethod.GET,"/api/project/**/member/**").permitAll()
                    .anyRequest()
                    .authenticated()
            )

            .oauth2Login(oauth -> oauth
                .userInfoEndpoint(c -> c.userService(authService))
                .successHandler(successHandler)
                .authorizationEndpoint()
                .baseUri("/login/oauth2/code/*")
                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
            )


//            .exceptionHandling(handler -> handler.authenticationEntryPoint(SecurityException))

            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(tokenExceptionFilter, tokenAuthenticationFilter.getClass());

        return http.build();
    }

}
