package com.openbook.openbook.util;

import com.openbook.openbook.api.user.response.TokenInfo;
import com.openbook.openbook.service.user.dto.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenProvider {

    @Value("${jwt.token.expired.time}")
    private Long TOKEN_EXPIRATION_TIME;
    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String USER_ID = "userId";
    private static final String USER_NICKNAME = "userNickname";
    private static final String USER_ROLE = "userRole";


    public String getTokenFrom(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public TokenInfo getInfoOf(String token) {
        Claims claims = getBody(token);;
        return TokenInfo.builder()
                .id(Long.valueOf(claims.get(USER_ID).toString()))
                .nickname(claims.get(USER_NICKNAME).toString())
                .role(claims.get(USER_ROLE).toString())
                .expires_in((claims.getExpiration().getTime() - System.currentTimeMillis())/1000)
                .build();
    }

    public boolean isExpired(String token){
        Date expiredDate = getBody(token).getExpiration();
        return expiredDate.before(new Date());
    }

    public String generateToken(Long id, String nickname, String role) {
        Claims claims = Jwts.claims();
        claims.put(USER_ID, id);
        claims.put(USER_NICKNAME, nickname);
        claims.put(USER_ROLE, role);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_EXPIRATION_TIME))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = getBody(token);
        UserDetails userDetails = new UserDetail(
                Long.valueOf(claims.get(USER_ID).toString()),
                claims.get(USER_NICKNAME).toString(),
                claims.get(USER_ROLE).toString()
        );
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
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
