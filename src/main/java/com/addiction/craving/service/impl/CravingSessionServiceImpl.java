package com.addiction.craving.service.impl;

import com.addiction.craving.entity.CravingSession;
import com.addiction.craving.entity.CravingSessionStatus;
import com.addiction.craving.repository.CravingSessionRepository;
import com.addiction.craving.service.CravingSessionService;
import com.addiction.craving.service.response.CravingSessionCompleteResponse;
import com.addiction.craving.service.response.CravingSessionResponse;
import com.addiction.craving.service.response.TodayCravingSummaryResponse;
import com.addiction.global.exception.AddictionException;
import com.addiction.global.security.SecurityService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CravingSessionServiceImpl implements CravingSessionService {
    private final CravingSessionRepository cravingSessionRepository;
    private final SecurityService securityService;
    private final UserReadService userReadService;
    private final Clock koreaClock;

    @Override
    public CravingSessionResponse start() {
        User user = currentUser();
        CravingSession session = CravingSession.start(user, LocalDateTime.now(koreaClock));
        return CravingSessionResponse.from(cravingSessionRepository.save(session));
    }

    @Override
    public CravingSessionCompleteResponse complete(Long sessionId) {
        User user = currentUser();
        CravingSession session = cravingSessionRepository.findWithLockById(sessionId)
                .orElseThrow(() -> new AddictionException("갈망 대응 기록을 찾을 수 없습니다."));

        if (!session.belongsTo(user.getId())) {
            throw new AddictionException("갈망 대응 기록을 찾을 수 없습니다.");
        }

        session.complete(LocalDateTime.now(koreaClock));
        return CravingSessionCompleteResponse.of(session, getTodayCompletedCount(user.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public TodayCravingSummaryResponse getTodaySummary() {
        return TodayCravingSummaryResponse.of(getTodayCompletedCount(currentUser().getId()));
    }

    private User currentUser() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        return userReadService.findById(userId);
    }

    private long getTodayCompletedCount(Long userId) {
        LocalDate today = LocalDate.now(koreaClock);
        LocalDateTime dayStart = today.atStartOfDay();
        return cravingSessionRepository.countByUser_IdAndStatusAndCompletedAtBetween(
                userId,
                CravingSessionStatus.COMPLETED,
                dayStart,
                dayStart.plusDays(1)
        );
    }
}
