package com.riyaindustries.repository;

import com.riyaindustries.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.published = true AND p.active = true ORDER BY p.id DESC")
    List<Product> findAllPublishedWithCategory();

    @Query("SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.id DESC")
    List<Product> findAllWithCategory();
}
