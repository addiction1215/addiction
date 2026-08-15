package com.addiction.user.users.repository;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.addiction.user.users.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.useYn = 'Y'")
    List<User> findAll();

    @Query("select distinct u from User u left join fetch u.pushes where u.useYn = 'Y'")
    List<User> findAllWithPushes();

	@Query("select u from User u where u.email = :email and u.useYn = 'Y'")
	Optional<User> findByEmail(@Param("email") String email);

	@Query("select u from User u where u.id = :id and u.useYn = 'Y'")
	Optional<User> findById(@Param("id") Long id);

    @Modifying
    @Query("""
            update User u
               set u.firstSmokingRecordedAt = :recordedAt
             where u.id = :userId
               and u.firstSmokingRecordedAt is null
            """)
    int markFirstSmokingRecorded(@Param("userId") Long userId,
                                  @Param("recordedAt") LocalDateTime recordedAt);

    boolean existsByEmail(String email);

}
