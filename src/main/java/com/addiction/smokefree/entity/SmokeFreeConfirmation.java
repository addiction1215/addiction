package com.addiction.smokefree.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(
        name = "smoke_free_confirmation",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_smoke_free_confirmation_user_date",
                columnNames = {"user_id", "confirmed_date"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmokeFreeConfirmation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "confirmed_date", nullable = false)
    private LocalDate confirmedDate;

    private SmokeFreeConfirmation(User user, LocalDate confirmedDate) {
        this.user = user;
        this.confirmedDate = confirmedDate;
    }

    public static SmokeFreeConfirmation confirm(User user, LocalDate confirmedDate) {
        return new SmokeFreeConfirmation(user, confirmedDate);
    }
}
