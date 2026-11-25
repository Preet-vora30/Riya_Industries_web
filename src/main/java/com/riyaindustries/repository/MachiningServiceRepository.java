package com.riyaindustries.repository;

import com.riyaindustries.entity.MachiningService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MachiningServiceRepository extends JpaRepository<MachiningService, Long> {

    @Query("SELECT s FROM MachiningService s JOIN FETCH s.category WHERE s.published = true ORDER BY s.displayOrder ASC, s.id ASC")
    List<MachiningService> findAllPublishedWithCategory();

    @Query("SELECT s FROM MachiningService s JOIN FETCH s.category WHERE s.published = true AND s.featured = true ORDER BY s.displayOrder ASC")
    List<MachiningService> findFeaturedWithCategory();

    @Query("SELECT s FROM MachiningService s JOIN FETCH s.category WHERE s.published = true AND s.category.slug = :categorySlug ORDER BY s.displayOrder ASC")
    List<MachiningService> findPublishedByCategorySlug(@Param("categorySlug") String categorySlug);

    @Query("SELECT s FROM MachiningService s JOIN FETCH s.category WHERE s.slug = :slug")
    Optional<MachiningService> findBySlugWithCategory(@Param("slug") String slug);

    @Query("SELECT s FROM MachiningService s JOIN FETCH s.category ORDER BY s.displayOrder ASC, s.id DESC")
    List<MachiningService> findAllWithCategoryOrderByDisplayOrder();
}
