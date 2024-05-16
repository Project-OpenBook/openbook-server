package com.openbook.openbook.config;

import com.openbook.openbook.global.exception.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception->{
                    exception.accessDeniedHandler(customAccessDeniedHandler);
                });

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/signup").permitAll();
            auth.anyRequest().authenticated();
        });

        return http.build();
    }
}
