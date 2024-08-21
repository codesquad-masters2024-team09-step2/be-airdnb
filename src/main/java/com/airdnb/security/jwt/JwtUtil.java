package com.airdnb.security.jwt;

import com.airdnb.global.constants.SecurityConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String signatureSecretKey;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(signatureSecretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Value("${jwt.token-valid-time}")
    private Long tokenValidTime;

    public String createToken(String id) {
        return Jwts.builder()
            .claim("id", id)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + tokenValidTime))
            .signWith(key)
            .compact();

    }

    public String getToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            return true;
        } catch (SignatureException e) {
            log.error("유효하지않은 서명: {}", token, e);
        } catch (MalformedJwtException e) {
            log.error("JWT 형식 오류: {}", token, e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT: {}", token, e);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰의 문자열 이상: {}", token, e);
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT: {}", token, e);
        }
        return false;
    }

    public String getId(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("id", String.class);
    }

    public Long getRemainingTime(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration().getTime()
            - System.currentTimeMillis();
    }
}
