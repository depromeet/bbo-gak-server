package com.server.bbo_gak.global.config.sentry;

import io.sentry.Sentry;
import io.sentry.SentryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentryConfig {

    @Value("${sentry.dsn}")
    private String dsn;

    @Bean
    public Sentry.OptionsConfiguration<SentryOptions> sentryOptions() {
        return options -> {
            options.setDsn(dsn);
            options.setBeforeSend((event, hint) -> {
                return event;
            });
        };
    }
}
