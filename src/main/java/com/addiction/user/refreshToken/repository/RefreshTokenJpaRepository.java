package com.addiction.user.refreshToken.repository;

import com.addiction.user.refreshToken.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserIdAndDeviceId(Long userId, String deviceId);

    @Modifying
    @Query(value = """
       INSERT INTO refresh_token (user_id, device_id, refresh_token, created_date, updated_date)
       VALUES (:userId, :deviceId, :refreshToken, NOW(6), NOW(6))
       ON DUPLICATE KEY UPDATE
          refresh_token = :refreshToken,
          updated_date = NOW(6)
       """, nativeQuery = true)
    void upsertByUserIdAndDeviceId(@Param("userId") Long userId,
                                   @Param("deviceId") String deviceId,
                                   @Param("refreshToken") String refreshToken);

    /**
     * 같은 refresh token으로 동시에 재발급을 요청하는 경우를 직렬화한다.
     * 먼저 조회한 트랜잭션이 행의 쓰기 락을 보유하고, 후속 요청은 해당 트랜잭션이 끝날 때까지 대기한다.
     * 이를 통해 먼저 성공한 요청이 토큰 해시를 교체하면, 기존 토큰을 사용한 후속 요청은 조회에 실패한다.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RefreshToken> findByRefreshTokenAndDeviceId(String refreshToken, String deviceId);
}