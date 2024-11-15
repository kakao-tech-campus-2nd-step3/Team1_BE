package team1.be.seamless.util.auth;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import team1.be.seamless.service.AuthService;
import team1.be.seamless.util.fiter.TokenAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;
    private final OAuth2SuccessHandler successHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;


    @Autowired
    public SecurityConfig(AuthService authService, OAuth2SuccessHandler successHandler,
                          TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.authService = authService;
        this.successHandler = successHandler;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors()
                .and()
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .headers(c -> c.frameOptions().disable())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(request -> request
                        .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs",
                                "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/login/**", "/api/auth/**", "/oauth2/**")
                        .permitAll()
                        .requestMatchers("/", "/error", "/favicon.ico", "/**/*.png", "/**/*.gif",
                                "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js")
                        .permitAll()
                        .requestMatchers("/h2-console/**", "/auth/**", "/api/test/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/project/**/member/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/project/**/member/**").permitAll()
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

                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
