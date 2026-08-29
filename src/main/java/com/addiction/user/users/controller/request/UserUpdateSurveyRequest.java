package com.addiction.user.users.controller.request;

import com.addiction.user.users.service.request.UserUpdateSurveyServiceRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserUpdateSurveyRequest {
    @NotNull(message = "답변 ID 목록은 필수입니다.")
    @Size(min = 1, message = "답변 ID 목록은 최소 1개 이상이어야 합니다.")
    private List<Long> answerId;

    @NotNull(message = "금연 목표는 필수입니다.")
    private String purpose;

    // @NotNull은 누락을, @Positive는 0 이하 값을 검증한다.
    @NotNull(message = "담배 가격은 필수입니다.")
    @Positive(message = "담배 가격은 0원 초과이어야 합니다.")
    private Integer cigarettePrice;

    // @NotNull은 누락을, @Positive는 0 이하 값을 검증한다.
    @NotNull(message = "담배 개비는 필수입니다.")
    @Positive(message = "담배 개비는 0개 초과이어야 합니다.")
    private Integer cigaretteCount;

    @Builder
    public UserUpdateSurveyRequest(List<Long> answerId, String purpose, Integer cigarettePrice, Integer cigaretteCount) {
        this.answerId = answerId;
        this.purpose = purpose;
        this.cigarettePrice = cigarettePrice;
        this.cigaretteCount = cigaretteCount;
    }

    public UserUpdateSurveyServiceRequest toServiceRequest() {
        return UserUpdateSurveyServiceRequest.builder()
                .answerId(answerId)
                .purpose(purpose)
                .cigarettePrice(cigarettePrice)
                .cigaretteCount(cigaretteCount)
                .build();
    }
}
