package com.addiction.faq.repository;

import com.addiction.faq.entity.Faq;
import com.addiction.faq.entity.enums.FaqCategory;
import com.addiction.faq.service.request.FaqListServiceRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.addiction.faq.entity.QFaq.faq;
import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class FaqQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Faq> findAll(FaqListServiceRequest request) {
        Pageable pageable = request.toPageable();

        List<Faq> content = queryFactory
                .selectFrom(faq)
                .where(
                        categoryEq(request.getCategory()),
                        keywordContains(request.getKeyword())
                )
                .orderBy(
                        faq.pinned.desc(),
                        faq.sortOrder.asc(),
                        faq.createdDate.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = ofNullable(
                queryFactory
                        .select(faq.count())
                        .from(faq)
                        .where(
                                categoryEq(request.getCategory()),
                                keywordContains(request.getKeyword())
                        )
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression categoryEq(FaqCategory category) {
        return category != null ? faq.category.eq(category) : null;
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return faq.title.containsIgnoreCase(keyword)
                .or(faq.description.containsIgnoreCase(keyword));
    }
}
