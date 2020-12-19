package com.wlminus.repository;

import com.wlminus.domain.Product;
import com.wlminus.domain.ShopOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the ShopOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
    @Query(value = "select shoporder from ShopOrder shoporder left join fetch shoporder.customer left join fetch shoporder.orderDescs left join fetch shoporder.province left join fetch shoporder.district left join fetch shoporder.ward order by shoporder.createdDate asc",
    countQuery = "select count (distinct shoporder) from ShopOrder shoporder")
    Page<ShopOrder> findAllOrder(Pageable pageable);

    @Query(value = "select shoporder from ShopOrder shoporder left join fetch shoporder.customer left join fetch shoporder.orderDescs left join fetch shoporder.province left join fetch shoporder.district left join fetch shoporder.ward where shoporder.orderStatus = :status order by shoporder.createdDate asc",
    countQuery = "select count (distinct shoporder) from ShopOrder shoporder where shoporder.orderStatus = :status")
    Page<ShopOrder> findWithStatus(Pageable pageable, @Param("status") String status);
}
