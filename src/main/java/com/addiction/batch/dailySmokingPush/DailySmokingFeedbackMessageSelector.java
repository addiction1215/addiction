package com.addiction.batch.dailySmokingPush;

import com.addiction.common.enums.DailySmokingFeedbackGrade;
import com.addiction.common.enums.DailySmokingFeedbackTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class DailySmokingFeedbackMessageSelector {

    private static final String MESSAGE_POOL_PATH = "daily-smoking-feedback-messages.json";

    private final ObjectMapper objectMapper;
    private DailySmokingFeedbackMessagePool messagePool;

    @PostConstruct
    void loadMessagePool() {
        try (InputStream inputStream = new ClassPathResource(MESSAGE_POOL_PATH).getInputStream()) {
            messagePool = objectMapper.readValue(inputStream, DailySmokingFeedbackMessagePool.class);
            validateMessagePool();
        } catch (IOException e) {
            throw new IllegalStateException("정기 흡연 피드백 문구 풀을 불러올 수 없습니다.", e);
        }
    }

    public DailySmokingFeedbackContent select(DailySmokingFeedbackGrade grade, DailySmokingFeedbackTime time) {
        List<String> statusMessages = messagePool.statusMessages().get(grade);
        List<String> actionMessages = messagePool.actionMessages().get(grade).get(time);
        return new DailySmokingFeedbackContent(selectOne(statusMessages), selectOne(actionMessages));
    }

    public DailySmokingFeedbackContent selectForNewUser(DailySmokingFeedbackTime time) {
        return DailySmokingFeedbackContent.onboarding(
                selectOne(messagePool.newUserActionMessages().get(time))
        );
    }

    private void validateMessagePool() {
        for (DailySmokingFeedbackGrade grade : DailySmokingFeedbackGrade.values()) {
            validate(messagePool.statusMessages().get(grade), grade + " 상태");
            for (DailySmokingFeedbackTime time : DailySmokingFeedbackTime.values()) {
                validate(messagePool.actionMessages().get(grade).get(time), grade + " " + time + " 행동");
            }
        }
        for (DailySmokingFeedbackTime time : DailySmokingFeedbackTime.values()) {
            validate(messagePool.newUserActionMessages().get(time), "신규 사용자 " + time + " 행동");
        }
    }

    private void validate(List<String> messages, String label) {
        if (messages == null || messages.isEmpty()) {
            throw new IllegalStateException(label + " 문구 풀이 비어 있습니다.");
        }
    }

    private String selectOne(List<String> messages) {
        return messages.get(ThreadLocalRandom.current().nextInt(messages.size()));
    }
}
