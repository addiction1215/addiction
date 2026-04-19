package com.addiction.faq.repository;

import com.addiction.faq.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqJpaRepository extends JpaRepository<Faq, Long> {
}
