package com.wlminus.repository;
import com.wlminus.domain.AppConst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AppConst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppConstRepository extends JpaRepository<AppConst, Long> {

}
