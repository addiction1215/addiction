package com.addiction.user.push.repository;

import com.addiction.user.push.entity.Push;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PushJpaRepository extends JpaRepository<Push, Long> {

    Optional<Push> findByDeviceId(String deviceId);

    @Modifying
    @Query(value = """
        INSERT INTO push (device_id, user_id, push_token, created_date, updated_date)
        VALUES (:deviceId, :userId, :pushToken, NOW(6), NOW(6))
        ON DUPLICATE KEY UPDATE
            user_id = :userId,
            push_token = :pushToken,
            updated_date = NOW(6)
        """, nativeQuery = true)
    void upsertByDeviceId(@Param("deviceId") String deviceId,
                          @Param("userId") Long userId,
                          @Param("pushToken") String pushToken);
}