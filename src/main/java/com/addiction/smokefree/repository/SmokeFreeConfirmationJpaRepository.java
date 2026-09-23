package com.addiction.smokefree.repository;

import com.addiction.smokefree.entity.SmokeFreeConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SmokeFreeConfirmationJpaRepository extends JpaRepository<SmokeFreeConfirmation, Long> {

    Optional<SmokeFreeConfirmation> findByUser_IdAndConfirmedDate(Long userId, LocalDate confirmedDate);

    List<SmokeFreeConfirmation> findAllByUser_IdAndConfirmedDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}
