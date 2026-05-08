package com.addiction.faq.service.request;

import com.addiction.faq.entity.enums.FaqCategory;
import com.addiction.global.page.request.PageInfoServiceRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class FaqListServiceRequest extends PageInfoServiceRequest {

    private FaqCategory category;
    private String keyword;
}
