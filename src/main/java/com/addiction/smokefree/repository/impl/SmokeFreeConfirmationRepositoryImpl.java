package com.addiction.smokefree.repository.impl;

import com.addiction.smokefree.entity.SmokeFreeConfirmation;
import com.addiction.smokefree.repository.SmokeFreeConfirmationJpaRepository;
import com.addiction.smokefree.repository.SmokeFreeConfirmationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SmokeFreeConfirmationRepositoryImpl implements SmokeFreeConfirmationRepository {

    private final SmokeFreeConfirmationJpaRepository smokeFreeConfirmationJpaRepository;

    @Override
    public SmokeFreeConfirmation save(SmokeFreeConfirmation confirmation) {
        return smokeFreeConfirmationJpaRepository.save(confirmation);
    }

    @Override
    public Optional<SmokeFreeConfirmation> findByUserIdAndConfirmedDate(Long userId, LocalDate confirmedDate) {
        return smokeFreeConfirmationJpaRepository.findByUser_IdAndConfirmedDate(userId, confirmedDate);
    }

    @Override
    public List<SmokeFreeConfirmation> findAllByUserIdAndConfirmedDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return smokeFreeConfirmationJpaRepository.findAllByUser_IdAndConfirmedDateBetween(userId, startDate, endDate);
    }
}
