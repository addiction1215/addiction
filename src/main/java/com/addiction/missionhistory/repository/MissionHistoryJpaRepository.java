package com.addiction.missionhistory.repository;

import com.addiction.common.enums.MissionStatus;
import com.addiction.missionhistory.entity.MissionHistory;
import com.addiction.user.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionHistoryJpaRepository extends JpaRepository<MissionHistory, Long> {
    /**
     * 사용자의 특정 상태 미션 조회
     */
    Optional<MissionHistory> findByUserIdAndStatus(User userId, MissionStatus status);
}
