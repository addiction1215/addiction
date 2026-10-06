package com.addiction.smokefree.service.impl;

import com.addiction.smokefree.repository.SmokeFreeConfirmationRepository;
import com.addiction.smokefree.service.SmokeFreeConfirmationReadService;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SmokeFreeConfirmationReadServiceImpl implements SmokeFreeConfirmationReadService {

    private final SmokeFreeConfirmationRepository smokeFreeConfirmationRepository;
    private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;
    private final UserCigaretteReadService userCigaretteReadService;

    @Override
    public Set<LocalDate> findConfirmedDates(Long userId, LocalDate startDate, LocalDate endDate) {
        return smokeFreeConfirmationRepository.findAllByUserIdAndConfirmedDateBetween(userId, startDate, endDate)
                .stream()
                .map(confirmation -> confirmation.getConfirmedDate())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LocalDate> findSuccessfulDates(Long userId, LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> confirmedDates = findConfirmedDates(userId, startDate, endDate);
        if (confirmedDates.isEmpty()) {
            return confirmedDates;
        }

        confirmedDates.removeAll(userCigaretteHistoryRepository
                .findSmokedDatesByUserIdAndDateBetween(userId, startDate, endDate));
        userCigaretteReadService.findSmokeTimesByUserIdAndPeriod(
                        userId, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay())
                .stream()
                .map(smokeTime -> smokeTime.toLocalDate())
                .forEach(confirmedDates::remove);
        return confirmedDates;
    }
}
