package com.addiction.smokefree.service.impl;

import com.addiction.global.exception.AddictionException;
import com.addiction.global.security.SecurityService;
import com.addiction.smokefree.entity.SmokeFreeConfirmation;
import com.addiction.smokefree.repository.SmokeFreeConfirmationRepository;
import com.addiction.smokefree.service.SmokeFreeConfirmationService;
import com.addiction.smokefree.service.request.SmokeFreeConfirmationServiceRequest;
import com.addiction.smokefree.service.response.SmokeFreeConfirmationResponse;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
@Transactional
public class SmokeFreeConfirmationServiceImpl implements SmokeFreeConfirmationService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

    private final SmokeFreeConfirmationRepository smokeFreeConfirmationRepository;
    private final UserCigaretteHistoryService userCigaretteHistoryService;
    private final SecurityService securityService;
    private final UserReadService userReadService;
    private final Clock koreaClock;

    @Override
    public SmokeFreeConfirmationResponse confirm(SmokeFreeConfirmationServiceRequest request) {
        User user = currentUser();
        LocalDate confirmedDate = parseDate(request.getDate());
        validateConfirmable(user, confirmedDate);

        SmokeFreeConfirmation confirmation = smokeFreeConfirmationRepository
                .findByUserIdAndConfirmedDate(user.getId(), confirmedDate)
                .orElseGet(() -> smokeFreeConfirmationRepository.save(
                        SmokeFreeConfirmation.confirm(user, confirmedDate)
                ));

        return SmokeFreeConfirmationResponse.from(confirmation);
    }

    private User currentUser() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        return userReadService.findById(userId);
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new AddictionException("날짜 형식이 올바르지 않습니다.");
        }
    }

    private void validateConfirmable(User user, LocalDate confirmedDate) {
        LocalDate today = LocalDate.now(koreaClock);
        if (confirmedDate.isAfter(today)) {
            throw new AddictionException("미래 날짜는 금연일로 확정할 수 없습니다.");
        }
        if (confirmedDate.isBefore(user.getCreatedDate().toLocalDate())) {
            throw new AddictionException("가입일 이전 날짜는 금연일로 확정할 수 없습니다.");
        }
        if (!userCigaretteHistoryService.findHistoryByDate(confirmedDate.format(DATE_FORMATTER)).isEmpty()) {
            throw new AddictionException("흡연 기록이 있는 날짜는 금연일로 확정할 수 없습니다.");
        }
    }
}
