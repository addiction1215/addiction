package com.addiction.smokefree.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.smokefree.entity.SmokeFreeConfirmation;
import com.addiction.smokefree.repository.SmokeFreeConfirmationRepository;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class SmokeFreeConfirmationReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private SmokeFreeConfirmationReadService readService;

    @Autowired
    private SmokeFreeConfirmationRepository confirmationRepository;

    @Autowired
    private Clock koreaClock;

    @Test
    void 확정일이_없으면_흡연_이력을_조회하지_않는다() {
        LocalDate today = LocalDate.now(koreaClock);
        assertThat(readService.findSuccessfulDates(1L, today.minusDays(3), today)).isEmpty();
        verify(userCigaretteHistoryRepository, never())
                .findSmokedDatesByUserIdAndDateBetween(any(), any(), any());
    }

    @Test
    void 확정일에서_MongoDB와_MySQL의_흡연일을_제외한다() {
        User user = userRepository.save(createUser("read@test.com", "password", SnsType.NORMAL, SettingStatus.COMPLETE));
        LocalDate today = LocalDate.now(koreaClock);
        LocalDate twoDaysAgo = today.minusDays(2);
        LocalDate yesterday = today.minusDays(1);
        confirmationRepository.save(SmokeFreeConfirmation.confirm(user, twoDaysAgo));
        confirmationRepository.save(SmokeFreeConfirmation.confirm(user, yesterday));
        confirmationRepository.save(SmokeFreeConfirmation.confirm(user, today));
        given(userCigaretteHistoryRepository.findSmokedDatesByUserIdAndDateBetween(user.getId(), twoDaysAgo, today))
                .willReturn(Set.of(twoDaysAgo));
        userCigaretteRepository.save(UserCigarette.createEntity(user, "테스트 주소", 0L, today.atTime(12, 0)));

        assertThat(readService.findSuccessfulDates(user.getId(), twoDaysAgo, today))
                .containsExactly(yesterday);
        verify(userCigaretteHistoryRepository)
                .findSmokedDatesByUserIdAndDateBetween(eq(user.getId()), eq(twoDaysAgo), eq(today));
    }
}
