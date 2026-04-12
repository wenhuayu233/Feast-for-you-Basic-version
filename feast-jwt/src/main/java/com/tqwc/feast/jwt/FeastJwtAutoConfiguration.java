package com.tqwc.feast.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class FeastJwtAutoConfiguration {

    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration-seconds:259200}") long expirationSeconds) {
        return new JwtTokenProvider(secret, expirationSeconds);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            @Value("${security.jwt.cookie-name:FFY_TOKEN}") String cookieName) {
        return new JwtAuthenticationFilter(jwtTokenProvider, cookieName);
    }
}
