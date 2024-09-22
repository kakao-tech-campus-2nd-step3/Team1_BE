package com.example.team1_be.util.auth;


import com.example.team1_be.DTO.AuthDTO.PrincipalDetails;
import com.example.team1_be.util.errorException.BaseHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtToken {

    private Key secretKey;
    private long tokenExpTime;

    public JwtToken(@Value("${jwt.secretKey}") String secretKey,
        @Value("${jwt.tokenExpTime}") long tokenExpTime) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.tokenExpTime = tokenExpTime;
    }

    public String createToken(Authentication authentication) {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime expirationDateTime = now.plusSeconds(tokenExpTime);

        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining());

        PrincipalDetails user = (PrincipalDetails) authentication.getPrincipal();

        Claims claims = Jwts.claims();
        claims.put("authentication", authorities);
        claims.put("email", user.getUser().getEmail());
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(expirationDateTime.toInstant()))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "만료된 토큰 입니다.");
        } catch (JwtException e) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다.");
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = validateToken(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

        return new UsernamePasswordAuthenticationToken(getEmail(token), token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(
            new SimpleGrantedAuthority(claims.get("authentication", String.class)));
    }

    public String getEmail(String token) {
        Claims claims = validateToken(token);
        return claims.get("email", String.class);
    }

}
