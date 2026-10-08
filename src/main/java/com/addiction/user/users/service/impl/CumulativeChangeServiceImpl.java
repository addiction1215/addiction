package com.addiction.user.users.service.impl;

import com.addiction.global.security.SecurityService;
import com.addiction.global.time.KoreaTime;
import com.addiction.smokefree.service.SmokeFreeConfirmationReadService;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.BenefitService;
import com.addiction.user.users.service.CumulativeChangeService;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.response.BenefitResponse;
import com.addiction.user.users.service.response.CumulativeChangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CumulativeChangeServiceImpl implements CumulativeChangeService {

    private final SecurityService securityService;
    private final UserReadService userReadService;
    private final BenefitService benefitService;
    private final UserCigaretteReadService userCigaretteReadService;
    private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;
    private final SmokeFreeConfirmationReadService smokeFreeConfirmationReadService;
    private final KoreaTime koreaTime;

    @Override
    public CumulativeChangeResponse findCumulativeChange() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        User user = userReadService.findById(userId);
        BenefitResponse benefit = benefitService.findMyBenefit();
        long totalSmokeFreeDays = smokeFreeConfirmationReadService.findSuccessfulDates(
                userId, user.getCreatedDate().toLocalDate(), koreaTime.today()).size();

        return CumulativeChangeResponse.builder()
                .savedMoney((long) user.getCigarettePrice() * totalSmokeFreeDays)
                .reducedCigaretteCount((long) user.getCigaretteCount() * totalSmokeFreeDays)
                .nonSmokingDays(benefit.getNonSmokingDays())
                .totalSmokeFreeDays(totalSmokeFreeDays)
                .longestAbstinenceSeconds(findLongestAbstinenceSeconds(userId))
                .build();
    }

    private Long findLongestAbstinenceSeconds(Long userId) {
        Long historyMax = userCigaretteHistoryRepository.findMaxSmokePatienceTimeByUserId(userId);

        LocalDate today = koreaTime.today();
        List<UserCigarette> todayCigarettes = userCigaretteReadService.findAllByUserIdAndCreatedDateBetween(
                userId, today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        Long todayMax = todayCigarettes.stream()
                .map(UserCigarette::getSmokePatienceTime)
                .filter(time -> time != null && time > 0)
                .max(Comparator.naturalOrder())
                .orElse(null);

        return Stream.of(historyMax, todayMax)
                .filter(time -> time != null)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }
}
