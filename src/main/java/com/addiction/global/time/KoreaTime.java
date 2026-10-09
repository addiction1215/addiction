package com.addiction.global.time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * 한국 시간(Asia/Seoul) 기준 현재 시각을 제공한다.
 *
 * 비즈니스 로직은 LocalDate.now() / LocalDateTime.now()를 직접 호출하지 않고 이 컴포넌트를 거친다.
 * JVM 기본 시간대(-Duser.timezone)에 의존하지 않고, 테스트에서 시계를 고정할 수 있도록 하기 위함이다.
 *
 * 하나의 작업 단위에서 날짜/시각이 여러 번 필요하면 여기서 반복 호출하지 말고,
 * 처음 읽은 값을 지역 변수로 두고 파생시킨다. 자정 경계에서 값이 어긋나는 것을 막기 위함이다.
 */
@Component
@RequiredArgsConstructor
public class KoreaTime {

    private final Clock koreaClock;

    public LocalDate today() {
        return LocalDate.now(koreaClock);
    }

    public LocalDateTime now() {
        return LocalDateTime.now(koreaClock);
    }

    public LocalDate yesterday() {
        return today().minusDays(1);
    }
}
