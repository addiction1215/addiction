package com.addiction.faq.controller.request;

import com.addiction.faq.entity.enums.FaqCategory;
import com.addiction.faq.service.request.FaqListServiceRequest;
import com.addiction.global.page.request.PageInfoRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FaqListRequest extends PageInfoRequest {

    private FaqCategory category;
    private String keyword;

    @Override
    public FaqListServiceRequest toServiceRequest() {
        return FaqListServiceRequest.builder()
                .page(getPage())
                .size(getSize())
                .category(category)
                .keyword(keyword)
                .build();
    }
}
