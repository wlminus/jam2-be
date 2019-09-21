package com.wlminus.repository;

import com.wlminus.domain.OrderDesc;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrderDesc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderDescRepository extends JpaRepository<OrderDesc, Long> {

}
