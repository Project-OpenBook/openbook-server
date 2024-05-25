package com.openbook.openbook.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("${jwt.token.expired.time}")
    private Long TOKEN_EXPIRATION_TIME;
    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;
    private static final String USER_ID = "userId";

    public boolean isExpired(String token){
        Date expiredDate = getBody(token).getExpiration();
        return expiredDate.before(new Date());
    }

    public Long getUserIdFromJwt(String token) {
        Claims claims = getBody(token);
        return Long.valueOf(claims.get(USER_ID).toString());
    }

    public String generateToken(Long userId){
        Claims claims = Jwts.claims();
        claims.put(USER_ID, userId);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_EXPIRATION_TIME))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getBody(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSignatureKey() {
        byte[] encodedKey = Base64.getEncoder().encode(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(encodedKey);
    }
}
