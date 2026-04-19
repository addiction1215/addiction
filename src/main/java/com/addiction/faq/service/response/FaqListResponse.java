package com.addiction.faq.service.response;

import com.addiction.faq.entity.Faq;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FaqListResponse {

    private final Long id;
    private final String title;
    private final String description;

    @Builder
    public FaqListResponse(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static FaqListResponse createResponse(Faq faq) {
        return FaqListResponse.builder()
                .id(faq.getId())
                .title(faq.getTitle())
                .description(faq.getDescription())
                .build();
    }
}
