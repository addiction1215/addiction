package com.addiction.user.users.entity;

import com.addiction.global.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "email_auth")
public class EmailAuth extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String authCode;

    private LocalDateTime expiredAt;

    @Builder
    private EmailAuth(String email, String authCode, LocalDateTime expiredAt) {
        this.email = email;
        this.authCode = authCode;
        this.expiredAt = expiredAt;
    }

    public static EmailAuth create(String email, String authCode) {
        return EmailAuth.builder()
                .email(email)
                .authCode(authCode)
                .expiredAt(LocalDateTime.now().plusMinutes(3))
                .build();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }
}
