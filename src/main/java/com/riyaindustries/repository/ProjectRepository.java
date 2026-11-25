package com.riyaindustries.repository;

import com.riyaindustries.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByPublishedTrueOrderByDisplayOrderAsc();
    List<Project> findByPublishedTrueAndMachiningTypeIgnoreCaseOrderByDisplayOrderAsc(String machiningType);
    List<Project> findAllByOrderByDisplayOrderAsc();
    long countByPublishedTrue();

    @Query("SELECT DISTINCT p.machiningType FROM Project p WHERE p.published = true ORDER BY p.machiningType ASC")
    List<String> findDistinctPublishedMachiningTypes();
}
