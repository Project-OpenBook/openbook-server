package com.openbook.openbook.global.util;

import com.openbook.openbook.user.controller.response.TokenInfo;
import com.openbook.openbook.user.dto.UserDetail;
import com.openbook.openbook.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserService userService;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String USER_ID = "userId";

    @Autowired
    public TokenProvider(UserService userService) {
        this.userService = userService;
    }

    public String getTokenFrom(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public TokenInfo getInfoOf(String token) {
        Claims claims = getBody(token);
        Long userId = Long.valueOf(claims.get(USER_ID).toString());
        UserDetail userDetails = UserDetail.of(userService.getUserOrException(userId));
        return TokenInfo.builder()
                .id(userId)
                .nickname(userDetails.nickname())
                .role(userDetails.role())
                .expires_in((claims.getExpiration().getTime() - System.currentTimeMillis())/1000)
                .build();
    }

    public boolean isExpired(String token){
        Date expiredDate = getBody(token).getExpiration();
        return expiredDate.before(new Date());
    }

    public String generateToken(Long id){
        Claims claims = Jwts.claims();
        claims.put(USER_ID, id);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_EXPIRATION_TIME))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = getBody(token);
        Long userId = Long.valueOf(claims.get(USER_ID).toString());
        UserDetails userDetails = UserDetail.of(userService.getUserOrException(userId));
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
