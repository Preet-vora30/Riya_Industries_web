package com.riyaindustries.repository;

import com.riyaindustries.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByActiveTrueOrderByDisplayOrderAsc();
    List<Machine> findAllByOrderByDisplayOrderAsc();
    Optional<Machine> findBySlug(String slug);
}
