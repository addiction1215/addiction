package com.addiction.mission.repository;

import com.addiction.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionJpaRepository extends JpaRepository<Mission, Long> {
}
