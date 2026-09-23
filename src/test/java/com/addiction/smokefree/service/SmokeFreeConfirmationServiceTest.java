package com.addiction.smokefree.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.global.exception.AddictionException;
import com.addiction.smokefree.repository.SmokeFreeConfirmationRepository;
import com.addiction.smokefree.service.request.SmokeFreeConfirmationServiceRequest;
import com.addiction.smokefree.service.response.SmokeFreeConfirmationResponse;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SmokeFreeConfirmationServiceTest extends IntegrationTestSupport {

    @Autowired
    private SmokeFreeConfirmationService smokeFreeConfirmationService;

    @Autowired
    private SmokeFreeConfirmationRepository smokeFreeConfirmationRepository;

    @Autowired
    private Clock koreaClock;

    @MockitoBean
    private UserCigaretteHistoryService userCigaretteHistoryService;

    @DisplayName("흡연 기록이 없는 오늘을 금연일로 확정한다")
    @Test
    void confirm() {
        User user = userRepository.save(createUser("smoke-free@test.com", "password", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));
        given(userCigaretteHistoryService.findHistoryByDate(anyToday())).willReturn(List.of());

        SmokeFreeConfirmationResponse response = smokeFreeConfirmationService.confirm(
                SmokeFreeConfirmationServiceRequest.builder().date(anyToday()).build()
        );

        assertThat(response.getDate()).isEqualTo(anyToday());
        assertThat(smokeFreeConfirmationRepository.findByUserIdAndConfirmedDate(user.getId(), LocalDate.now(koreaClock)))
                .isPresent();
    }

    @DisplayName("흡연 기록이 있는 날짜는 금연일로 확정할 수 없다")
    @Test
    void rejectSmokedDay() {
        User user = userRepository.save(createUser("smoke-free-smoked@test.com", "password", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));
        given(userCigaretteHistoryService.findHistoryByDate(anyToday()))
                .willReturn(List.of(mock(UserCigaretteHistoryResponse.class)));

        assertThatThrownBy(() -> smokeFreeConfirmationService.confirm(
                SmokeFreeConfirmationServiceRequest.builder().date(anyToday()).build()
        )).isInstanceOf(AddictionException.class);
    }

    private String anyToday() {
        return LocalDate.now(koreaClock).format(DateTimeFormatter.BASIC_ISO_DATE);
    }
}
