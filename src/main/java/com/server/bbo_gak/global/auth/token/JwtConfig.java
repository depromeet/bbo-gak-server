package com.server.bbo_gak.global.auth.token;

import com.vivid.apiserver.global.auth.application.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.password}")
    private String secretKey;

    @Bean
    public TokenProvider jwtProvider() {
        return new TokenProvider(secretKey);
    }
}
