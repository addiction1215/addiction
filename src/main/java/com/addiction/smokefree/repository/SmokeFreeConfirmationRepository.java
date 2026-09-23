package com.addiction.smokefree.repository;

import com.addiction.smokefree.entity.SmokeFreeConfirmation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SmokeFreeConfirmationRepository {

    SmokeFreeConfirmation save(SmokeFreeConfirmation confirmation);

    Optional<SmokeFreeConfirmation> findByUserIdAndConfirmedDate(Long userId, LocalDate confirmedDate);

    List<SmokeFreeConfirmation> findAllByUserIdAndConfirmedDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}
