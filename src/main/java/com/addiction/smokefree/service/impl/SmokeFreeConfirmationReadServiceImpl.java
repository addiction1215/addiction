package com.addiction.smokefree.service.impl;

import com.addiction.smokefree.repository.SmokeFreeConfirmationRepository;
import com.addiction.smokefree.service.SmokeFreeConfirmationReadService;
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

    @Override
    public Set<LocalDate> findConfirmedDates(Long userId, LocalDate startDate, LocalDate endDate) {
        return smokeFreeConfirmationRepository.findAllByUserIdAndConfirmedDateBetween(userId, startDate, endDate)
                .stream()
                .map(confirmation -> confirmation.getConfirmedDate())
                .collect(Collectors.toSet());
    }
}
