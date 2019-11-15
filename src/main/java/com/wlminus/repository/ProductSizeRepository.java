package com.wlminus.repository;
import com.wlminus.domain.ProductSize;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProductSize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {

}
