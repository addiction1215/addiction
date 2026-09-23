package com.addiction.smokefree.controller.request;

import com.addiction.smokefree.service.request.SmokeFreeConfirmationServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SmokeFreeConfirmationRequest {

    @NotBlank(message = "날짜는 필수입니다.")
    @Pattern(regexp = "\\d{8}", message = "날짜는 yyyyMMdd 형식이어야 합니다.")
    private String date;

    public SmokeFreeConfirmationServiceRequest toServiceRequest() {
        return SmokeFreeConfirmationServiceRequest.builder()
                .date(date)
                .build();
    }
}
