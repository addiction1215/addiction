package com.addiction.faq.entity;

import com.addiction.faq.entity.enums.FaqCategory;
import com.addiction.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Faq extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FaqCategory category;

    private boolean pinned;

    private int sortOrder;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder
    public Faq(Long id, FaqCategory category, boolean pinned, int sortOrder, String title, String description) {
        this.id = id;
        this.category = category;
        this.pinned = pinned;
        this.sortOrder = sortOrder;
        this.title = title;
        this.description = description;
    }
}
