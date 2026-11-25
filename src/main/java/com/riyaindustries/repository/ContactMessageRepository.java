package com.riyaindustries.repository;

import com.riyaindustries.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    long countByReadFalse();
    Page<ContactMessage> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
