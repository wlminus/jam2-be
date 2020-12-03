package com.wlminus.repository;

import com.wlminus.domain.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the ProductSize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
    Optional<ProductSize> findBySizeName(String name);
}
