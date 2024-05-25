package com.openbook.openbook.configuration;

import com.openbook.openbook.global.util.JwtUtils;
import com.openbook.openbook.global.exception.OpenBookException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final static String TOKEN_PREFIX = "Bearer ";
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token = getTokenFromRequest(request);
            if(token == null) {
                throw new OpenBookException(HttpStatus.BAD_REQUEST, "토큰 정보가 잘못되었습니다.");
            }
            if(jwtUtils.isExpired(token)) {
                throw new OpenBookException(HttpStatus.BAD_REQUEST, "만료된 토큰입니다.");
            }
            Long userId = jwtUtils.getUserIdFromJwt(token);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userId.toString(), null, null
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (OpenBookException e) {
            log.error("[ERROR] {}", e.getMessage());
        }
        filterChain.doFilter(request,response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

}
