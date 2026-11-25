package com.riyaindustries.repository;

import com.riyaindustries.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByUsernameAndActiveTrue(String username);
    boolean existsByUsername(String username);
}
