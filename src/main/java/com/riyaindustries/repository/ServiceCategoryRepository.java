package com.riyaindustries.repository;

import com.riyaindustries.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    List<ServiceCategory> findByActiveTrueOrderByDisplayOrderAsc();
    List<ServiceCategory> findAllByOrderByDisplayOrderAsc();
    Optional<ServiceCategory> findBySlug(String slug);
}
