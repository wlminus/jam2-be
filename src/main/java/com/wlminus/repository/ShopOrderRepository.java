package com.wlminus.repository;

import com.wlminus.domain.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShopOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {

}
