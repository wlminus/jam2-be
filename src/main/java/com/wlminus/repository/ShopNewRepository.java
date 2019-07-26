package com.wlminus.repository;

import com.wlminus.domain.ShopNew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ShopNew entity.
 */
@Repository
public interface ShopNewRepository extends JpaRepository<ShopNew, Long> {

    @Query(value = "select distinct shopNew from ShopNew shopNew left join fetch shopNew.tags",
        countQuery = "select count(distinct shopNew) from ShopNew shopNew")
    Page<ShopNew> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct shopNew from ShopNew shopNew left join fetch shopNew.tags")
    List<ShopNew> findAllWithEagerRelationships();

    @Query("select shopNew from ShopNew shopNew left join fetch shopNew.tags where shopNew.id =:id")
    Optional<ShopNew> findOneWithEagerRelationships(@Param("id") Long id);

}
