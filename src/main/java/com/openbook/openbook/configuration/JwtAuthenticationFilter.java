package com.openbook.openbook.configuration;

import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.util.TokenProvider;
import com.openbook.openbook.exception.OpenBookException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token = tokenProvider.getTokenFrom(request);
            if(token == null) {
                filterChain.doFilter(request,response);
                return;
            }
            if(tokenProvider.isExpired(token)) {
                throw new OpenBookException(ErrorCode.INVALID_TOKEN);
            }

            UsernamePasswordAuthenticationToken authenticationToken = tokenProvider.getAuthentication(token);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (OpenBookException e) {
            log.error("[ERROR] {}", e.getMessage());
        }
        filterChain.doFilter(request,response);
    }

}
