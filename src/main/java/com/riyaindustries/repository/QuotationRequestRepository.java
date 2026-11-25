package com.riyaindustries.repository;

import com.riyaindustries.entity.QuotationRequest;
import com.riyaindustries.entity.WorkStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface QuotationRequestRepository extends JpaRepository<QuotationRequest, Long> {

    long countByStatusIn(Collection<WorkStatus> statuses);

    Page<QuotationRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<QuotationRequest> findByStatusOrderByCreatedAtDesc(WorkStatus status, Pageable pageable);

    Page<QuotationRequest> findByCustomerNameContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrReferenceCodeContainingIgnoreCaseOrderByCreatedAtDesc(
        String customerName, String companyName, String referenceCode, Pageable pageable);

    @Query("SELECT q FROM QuotationRequest q LEFT JOIN FETCH q.attachments WHERE q.id = :id")
    Optional<QuotationRequest> findByIdWithAttachments(@Param("id") Long id);
}
