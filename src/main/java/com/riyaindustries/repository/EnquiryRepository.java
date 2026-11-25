package com.riyaindustries.repository;

import com.riyaindustries.entity.Enquiry;
import com.riyaindustries.entity.WorkStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    long countByStatus(WorkStatus status);
    Page<Enquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Enquiry> findByStatusOrderByCreatedAtDesc(WorkStatus status, Pageable pageable);
    Page<Enquiry> findByNameContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrPhoneContainingIgnoreCaseOrderByCreatedAtDesc(
        String name, String companyName, String phone, Pageable pageable);
}
