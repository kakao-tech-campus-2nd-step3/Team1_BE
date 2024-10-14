package team1.BE.seamless.util.auth;


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
import team1.BE.seamless.DTO.AuthDTO.PrincipalDetails;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.UserEntity;
import team1.BE.seamless.entity.enums.Role;
import team1.BE.seamless.util.errorException.RuntimeHandler;

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

    public String createMemberToken(MemberEntity member) {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime expirationDateTime = now.plusSeconds(tokenExpTime);

        Claims claims = Jwts.claims();
        claims.put("authentication", Role.MEMBER.toString());
        claims.put("email", member.getEmail());
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(expirationDateTime.toInstant()))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 테스트외 절대 사용 금지
     * */
    public String createUserToken(UserEntity user) {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime expirationDateTime = now.plusSeconds(tokenExpTime);

        Claims claims = Jwts.claims();
        claims.put("authentication", Role.MEMBER.toString());
        claims.put("email", user.getEmail());
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
            throw new RuntimeHandler(HttpStatus.UNAUTHORIZED, "만료된 토큰 입니다.");
        } catch (JwtException e) {
            throw new RuntimeHandler(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다.");
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

    public String getRole(String token) {
        Claims claims = validateToken(token);
        return claims.get("authentication", String.class);
    }

}
