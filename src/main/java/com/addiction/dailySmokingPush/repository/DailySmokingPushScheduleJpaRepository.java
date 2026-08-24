package com.addiction.dailySmokingPush.repository;

import com.addiction.dailySmokingPush.entity.DailySmokingPushSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface DailySmokingPushScheduleJpaRepository extends JpaRepository<DailySmokingPushSchedule, Long> {

    List<DailySmokingPushSchedule> findByUserIdOrderBySlot(Long userId);

    @Query("""
            select distinct s from DailySmokingPushSchedule s
            join fetch s.user u
            left join fetch u.pushes
            where s.enabled = true and s.sendTime = :sendTime
            """)
    List<DailySmokingPushSchedule> findEnabledDueAt(@Param("sendTime") LocalTime sendTime);
}
