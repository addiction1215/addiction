package com.addiction.user.userCigarette.service.impl;

import com.addiction.global.security.SecurityService;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.repository.UserCigaretteRepository;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigarette.service.UserCigaretteService;
import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.request.UserCigaretteChangeServiceRequest;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.repository.UserRepository;
import com.addiction.user.users.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCigaretteServiceImpl implements UserCigaretteService {

    private final UserCigaretteReadService userCigaretteReadService;
    private final SecurityService securityService;
    private final UserReadService userReadService;

    private final UserCigaretteRepository userCigaretteRepository;
    private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;
    private final UserRepository userRepository;
    private final Clock koreaClock;

    @Override
    public Long changeCigarette(UserCigaretteChangeServiceRequest userCigaretteChangeServiceRequest) {
        User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
        if (userCigaretteChangeServiceRequest.getChangeType().equals(ChangeType.ADD)) {
            LocalDateTime now = LocalDateTime.now(koreaClock);
            long intervalSeconds = 0;

            if (user.getLastSmokeAt() != null) {
                intervalSeconds = Duration.between(user.getLastSmokeAt(), now).getSeconds();
            } else {
                // lastSmokeAt 백필 전 기존 사용자에 대한 호환 처리
                UserCigarette lastCigarette = userCigaretteReadService.findLatestByUserId(user.getId());
                if (lastCigarette != null) {
                    intervalSeconds = Duration.between(lastCigarette.getSmokeTime(), now).getSeconds();
                }
            }
            UserCigarette userCigarette = UserCigarette.createEntity(
                    user, userCigaretteChangeServiceRequest.getAddress(), intervalSeconds, now
            );
            userCigaretteRepository.save(userCigarette);
            user.updateLastSmoking(now, userCigaretteChangeServiceRequest.getAddress());
            userRepository.markFirstSmokingRecorded(user.getId(), now);
            return user.getId();
        }
        UserCigarette latestCigarette = userCigaretteReadService.findLatestByUserId(user.getId());
        if (latestCigarette == null) {
            return user.getId();
        }

        userCigaretteRepository.delete(latestCigarette);
        updateLastSmokingAfterDeletion(user);
        return user.getId();
    }

    private void updateLastSmokingAfterDeletion(User user) {
        UserCigarette latestCigarette = userCigaretteReadService.findLatestByUserId(user.getId());
        if (latestCigarette != null) {
            user.updateLastSmoking(latestCigarette.getSmokeTime(), latestCigarette.getAddress());
            return;
        }

        CigaretteHistoryDocument latestHistory = userCigaretteHistoryRepository.findLatestByUserId(user.getId());
        if (latestHistory == null || latestHistory.getHistory() == null) {
            user.updateLastSmoking(null, null);
            return;
        }

        latestHistory.getHistory().stream()
                .filter(history -> history.getSmokeTime() != null)
                .max(java.util.Comparator.comparing(CigaretteHistoryDocument.History::getSmokeTime))
                .ifPresentOrElse(
                        history -> user.updateLastSmoking(history.getSmokeTime(), history.getAddress()),
                        () -> user.updateLastSmoking(null, null)
                );
    }

    @Override
    public void deleteAll(List<UserCigarette> userCigarettes) {
        userCigaretteRepository.deleteAllInBatch(userCigarettes);
    }
}
