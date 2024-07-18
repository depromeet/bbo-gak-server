package com.server.bbo_gak.global.config.properties;

import com.server.bbo_gak.global.security.jwt.entity.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
    JwtProperties.class
})
@Configuration
public class PropertiesConfig {

}
