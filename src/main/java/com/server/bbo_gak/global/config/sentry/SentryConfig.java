package com.server.bbo_gak.global.config.sentry;

import com.server.bbo_gak.global.error.exception.BusinessException;
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
                // 특정 예외 필터링 로직
                if (event.getThrowable() != null && event.getThrowable() instanceof BusinessException) {
                    return null; // 이 이벤트는 Sentry로 전송되지 않음
                }
                return event; // 필터링하지 않을 경우, 이벤트 반환
            });
        };
    }
}
