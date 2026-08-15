package com.addiction.global.config;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "koreaDateTimeProvider")
public class JpaAuditingConfig {

    public static final String KOREA_ZONE_ID = "Asia/Seoul";

    @Bean
    public Clock koreaClock() {
        return Clock.system(ZoneId.of(KOREA_ZONE_ID));
    }

    @Bean
    public DateTimeProvider koreaDateTimeProvider(Clock koreaClock) {
        return () -> Optional.of(LocalDateTime.now(koreaClock));
    }
}
