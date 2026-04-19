package com.addiction.faq.repository.impl;

import com.addiction.faq.entity.Faq;
import com.addiction.faq.repository.FaqJpaRepository;
import com.addiction.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FaqRepositoryImpl implements FaqRepository {

    private final FaqJpaRepository faqJpaRepository;

    @Override
    public List<Faq> findAll() {
        return faqJpaRepository.findAll();
    }
}
