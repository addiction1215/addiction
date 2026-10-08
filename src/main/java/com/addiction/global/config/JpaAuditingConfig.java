package com.addiction.global.config;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.addiction.global.time.KoreaTime;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "koreaDateTimeProvider")
public class JpaAuditingConfig {

    public static final String KOREA_ZONE_ID = "Asia/Seoul";

    /**
     * 애플리케이션 전역 시계. 직접 주입받지 말고 {@link KoreaTime}을 사용한다.
     */
    @Bean
    public Clock koreaClock() {
        return Clock.system(ZoneId.of(KOREA_ZONE_ID));
    }

    @Bean
    public DateTimeProvider koreaDateTimeProvider(KoreaTime koreaTime) {
        return () -> Optional.of(koreaTime.now());
    }
}
