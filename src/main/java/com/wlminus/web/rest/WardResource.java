package com.wlminus.web.rest;

import com.wlminus.domain.Ward;
import com.wlminus.repository.WardRepository;
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
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.wlminus.domain.Ward}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WardResource {

    private final Logger log = LoggerFactory.getLogger(WardResource.class);

    private static final String ENTITY_NAME = "ward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WardRepository wardRepository;

    public WardResource(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    /**
     * {@code POST  /wards} : Create a new ward.
     *
     * @param ward the ward to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ward, or with status {@code 400 (Bad Request)} if the ward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wards")
    public ResponseEntity<Ward> createWard(@Valid @RequestBody Ward ward) throws URISyntaxException {
        log.debug("REST request to save Ward : {}", ward);
        if (ward.getId() != null) {
            throw new BadRequestAlertException("A new ward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ward result = wardRepository.save(ward);
        return ResponseEntity.created(new URI("/api/wards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wards} : Updates an existing ward.
     *
     * @param ward the ward to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ward,
     * or with status {@code 400 (Bad Request)} if the ward is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ward couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wards")
    public ResponseEntity<Ward> updateWard(@Valid @RequestBody Ward ward) throws URISyntaxException {
        log.debug("REST request to update Ward : {}", ward);
        if (ward.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ward result = wardRepository.save(ward);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ward.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /wards} : get all the wards.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wards in body.
     */
    @GetMapping("/wards")
    public ResponseEntity<List<Ward>> getAllWards(Pageable pageable) {
        log.debug("REST request to get a page of Wards");
        Page<Ward> page = wardRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wards/:id} : get the "id" ward.
     *
     * @param id the id of the ward to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ward, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wards/{id}")
    public ResponseEntity<Ward> getWard(@PathVariable Long id) {
        log.debug("REST request to get Ward : {}", id);
        Optional<Ward> ward = wardRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ward);
    }

    /**
     * {@code DELETE  /wards/:id} : delete the "id" ward.
     *
     * @param id the id of the ward to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wards/{id}")
    public ResponseEntity<Void> deleteWard(@PathVariable Long id) {
        log.debug("REST request to delete Ward : {}", id);
        wardRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
