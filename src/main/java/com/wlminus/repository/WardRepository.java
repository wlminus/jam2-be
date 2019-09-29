package com.wlminus.repository;

import com.wlminus.domain.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Ward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findAllByDistrictId(Long districtId);
}
