package com.addiction.faq.repository;

import com.addiction.faq.entity.Faq;

import java.util.List;

public interface FaqRepository {

    List<Faq> findAll();
}
