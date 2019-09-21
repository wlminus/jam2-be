package com.wlminus.web.rest;

import com.wlminus.domain.AppConst;
import com.wlminus.repository.AppConstRepository;
import com.wlminus.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.wlminus.domain.AppConst}.
 */
@RestController
@RequestMapping("/api")
public class AppConstResource {

    private final Logger log = LoggerFactory.getLogger(AppConstResource.class);

    private static final String ENTITY_NAME = "appConst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppConstRepository appConstRepository;

    public AppConstResource(AppConstRepository appConstRepository) {
        this.appConstRepository = appConstRepository;
    }

    /**
     * {@code POST  /app-consts} : Create a new appConst.
     *
     * @param appConst the appConst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appConst, or with status {@code 400 (Bad Request)} if the appConst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/app-consts")
    public ResponseEntity<AppConst> createAppConst(@Valid @RequestBody AppConst appConst) throws URISyntaxException {
        log.debug("REST request to save AppConst : {}", appConst);
        if (appConst.getId() != null) {
            throw new BadRequestAlertException("A new appConst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppConst result = appConstRepository.save(appConst);
        return ResponseEntity.created(new URI("/api/app-consts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /app-consts} : Updates an existing appConst.
     *
     * @param appConst the appConst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appConst,
     * or with status {@code 400 (Bad Request)} if the appConst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appConst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/app-consts")
    public ResponseEntity<AppConst> updateAppConst(@Valid @RequestBody AppConst appConst) throws URISyntaxException {
        log.debug("REST request to update AppConst : {}", appConst);
        if (appConst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AppConst result = appConstRepository.save(appConst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appConst.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /app-consts} : get all the appConsts.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appConsts in body.
     */
    @GetMapping("/app-consts")
    public ResponseEntity<List<AppConst>> getAllAppConsts(Pageable pageable) {
        log.debug("REST request to get a page of AppConsts");
        Page<AppConst> page = appConstRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-consts/:id} : get the "id" appConst.
     *
     * @param id the id of the appConst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appConst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/app-consts/{id}")
    public ResponseEntity<AppConst> getAppConst(@PathVariable Long id) {
        log.debug("REST request to get AppConst : {}", id);
        Optional<AppConst> appConst = appConstRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(appConst);
    }

    /**
     * {@code DELETE  /app-consts/:id} : delete the "id" appConst.
     *
     * @param id the id of the appConst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/app-consts/{id}")
    public ResponseEntity<Void> deleteAppConst(@PathVariable Long id) {
        log.debug("REST request to delete AppConst : {}", id);
        appConstRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/app-consts/key/{key}")
    public ResponseEntity<AppConst> getOneAppConstByKey(@PathVariable String key) {
        log.debug("REST request to get AppConst : {}", key);
        Optional<AppConst> appConst = appConstRepository.findOneByKey(key);
        return ResponseUtil.wrapOrNotFound(appConst);
    }

    @GetMapping("/app-consts/list/key/{key}")
    public ResponseEntity<List<AppConst>> getManyAppConstByKey(@PathVariable String key) {
        log.debug("REST request to get AppConst : {}", key);
        List<AppConst> appConst = appConstRepository.findManyByKey(key);
        return ResponseEntity.ok().body(appConst);
    }
}
