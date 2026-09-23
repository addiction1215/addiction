package com.addiction.smokefree.service;

import java.time.LocalDate;
import java.util.Set;

public interface SmokeFreeConfirmationReadService {

    Set<LocalDate> findConfirmedDates(Long userId, LocalDate startDate, LocalDate endDate);
}
