package com.addiction.faq.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.faq.entity.Faq;
import com.addiction.faq.entity.enums.FaqCategory;
import com.addiction.faq.repository.FaqJpaRepository;
import com.addiction.faq.service.request.FaqListServiceRequest;
import com.addiction.faq.service.response.FaqListResponse;
import com.addiction.global.page.response.PageCustom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class FaqReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private FaqReadService faqReadService;
    @Autowired
    private FaqJpaRepository faqJpaRepository;

    @AfterEach
    void tearDownFaq() {
        faqJpaRepository.deleteAllInBatch();
    }

    @DisplayName("FAQ 목록 조회 시 useYn이 Y인 데이터만 반환한다.")
    @Test
    void FAQ_목록_조회시_useYn이_Y인_데이터만_반환한다() {
        // given
        faqJpaRepository.save(createFaq("노출 FAQ", "Y", 1));
        faqJpaRepository.save(createFaq("숨김 FAQ", "N", 2));

        FaqListServiceRequest request = FaqListServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        // when
        PageCustom<FaqListResponse> response = faqReadService.findAll(request);

        // then
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent())
                .extracting(FaqListResponse::getTitle)
                .containsExactly("노출 FAQ");
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(1L);
    }

    private Faq createFaq(String title, String useYn, int sortOrder) {
        return Faq.builder()
                .useYn(useYn)
                .category(FaqCategory.INFO_FRIEND)
                .pinned(false)
                .sortOrder(sortOrder)
                .title(title)
                .description(title + " 설명")
                .build();
    }
}
