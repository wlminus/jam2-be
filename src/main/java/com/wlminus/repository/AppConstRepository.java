package com.wlminus.repository;

import com.wlminus.domain.AppConst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the AppConst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppConstRepository extends JpaRepository<AppConst, Long> {

    @Query("select appConst from AppConst appConst where appConst.constKey=:configKey")
    Optional<AppConst> findOneByKey(@Param("configKey") String configKey);

    @Query("select appConst from AppConst appConst where appConst.constKey=:configKey")
    List<AppConst> findManyByKey(@Param("configKey") String configKey);
}
