package com.addiction.missionhistory.repository;

import com.addiction.missionhistory.entity.MissionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionHistoryJpaRepository extends JpaRepository<MissionHistory, Long> {
}
