package com.wlminus.repository;

import com.wlminus.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select distinct product from Product product left join fetch product.media left join fetch product.productSizes left join fetch product.tags",
        countQuery = "select count(distinct product) from Product product")
    Page<Product> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct product from Product product left join fetch product.media left join fetch product.productSizes left join fetch product.tags")
    List<Product> findAllWithEagerRelationships();

    @Query("select product from Product product left join fetch product.media left join fetch product.productSizes left join fetch product.tags where product.id =:id")
    Optional<Product> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select product from Product product left join fetch product.media where product.id =:id")
    Optional<Product> findOneWithMedia(@Param("id") Long id);

    @Query("select product from Product product left join fetch product.media left join fetch product.productSizes left join fetch product.tags where product.slug =:slug")
    Optional<Product> findOneBySlug(@Param("slug") String slug);

    @Query(value = "select distinct product from Product product left join fetch product.media",
        countQuery = "select count(distinct product) from Product product")
    Page<Product> findAllWithMedia(Pageable pageable);

    @Query(value = "select distinct product from Product product left join fetch product.media left join fetch product.category where product.category.slug = :catSlug",
        countQuery = "select count(distinct product) from Product product where product.category.slug = :catSlug")
    Page<Product> findAllByCategorySlug(@Param("catSlug") String catSlug, Pageable pageable);

    @Query(value = "select distinct product from Product product left join fetch product.media left join fetch product.productSizes left join fetch product.tags where product.name like %:query% or product.slug like %:query% or product.productCode like %:query%",
        countQuery = "select count(distinct product) from Product product where product.name like %:query% or product.slug like %:query% or product.productCode like %:query%")
    Page<Product> searchProduct(@Param("query") String query, Pageable pageable);

    @Query(value = "select distinct product from Product product left join fetch product.media")
    List<Product> findAllByIsValidIsTrue();

    @Query(value = "select distinct product from Product product left join fetch product.media where product.id in :arr")
    List<Product> findRelated(@Param("arr") List<Long> listID, Pageable pageable);

    long count();
}
