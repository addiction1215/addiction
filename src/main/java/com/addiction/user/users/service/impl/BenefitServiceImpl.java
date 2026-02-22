package com.addiction.user.users.service.impl;

import com.addiction.global.security.SecurityService;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.BenefitService;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.response.BenefitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BenefitServiceImpl implements BenefitService {

    private static final int CIGARETTES_PER_PACK = 20;

    private final SecurityService securityService;
    private final UserReadService userReadService;
    private final UserCigaretteReadService userCigaretteReadService;

    @Override
    public BenefitResponse findMyBenefit() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        User user = userReadService.findById(userId);

        LocalDate lastSmokeDate = findLastSmokeDate(userId, user);
        long nonSmokingDays = ChronoUnit.DAYS.between(lastSmokeDate, LocalDate.now());

        long dailySavedMoney = (long) user.getCigaretteCount() * user.getCigarettePrice() / CIGARETTES_PER_PACK;
        long savedMoney = nonSmokingDays * dailySavedMoney;

        return BenefitResponse.createResponse(nonSmokingDays, savedMoney, dailySavedMoney);
    }

    /**
     * 최신 흡연 날짜 조회
     * 1. RDBMS(오늘 기록) → 2. User.startDate (0시 배치에서 갱신됨)
     */
    private LocalDate findLastSmokeDate(Long userId, User user) {
        UserCigarette latestCigarette = userCigaretteReadService.findLatestByUserId(userId);
        if (latestCigarette != null) {
            return latestCigarette.getSmokeTime().toLocalDate();
        }
        return user.getStartDate().toLocalDate();
    }
}
