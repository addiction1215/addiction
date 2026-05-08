package com.addiction.faq.service.response;

import com.addiction.faq.entity.Faq;
import com.addiction.faq.entity.enums.FaqCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FaqListResponse {

    private final Long id;
    private final FaqCategory category;
    private final boolean pinned;
    private final int sortOrder;
    private final String title;
    private final String description;

    @Builder
    public FaqListResponse(Long id, FaqCategory category, boolean pinned, int sortOrder, String title, String description) {
        this.id = id;
        this.category = category;
        this.pinned = pinned;
        this.sortOrder = sortOrder;
        this.title = title;
        this.description = description;
    }

    public static FaqListResponse createResponse(Faq faq) {
        return FaqListResponse.builder()
                .id(faq.getId())
                .category(faq.getCategory())
                .pinned(faq.isPinned())
                .sortOrder(faq.getSortOrder())
                .title(faq.getTitle())
                .description(faq.getDescription())
                .build();
    }
}
