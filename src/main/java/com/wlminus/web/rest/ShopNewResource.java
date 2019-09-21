package com.wlminus.web.rest;

import com.wlminus.domain.ShopNew;
import com.wlminus.repository.ShopNewRepository;
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
 * REST controller for managing {@link com.wlminus.domain.ShopNew}.
 */
@RestController
@RequestMapping("/api")
public class ShopNewResource {

    private final Logger log = LoggerFactory.getLogger(ShopNewResource.class);

    private static final String ENTITY_NAME = "shopNew";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopNewRepository shopNewRepository;

    public ShopNewResource(ShopNewRepository shopNewRepository) {
        this.shopNewRepository = shopNewRepository;
    }

    /**
     * {@code POST  /shop-news} : Create a new shopNew.
     *
     * @param shopNew the shopNew to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopNew, or with status {@code 400 (Bad Request)} if the shopNew has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shop-news")
    public ResponseEntity<ShopNew> createShopNew(@Valid @RequestBody ShopNew shopNew) throws URISyntaxException {
        log.debug("REST request to save ShopNew : {}", shopNew);
        if (shopNew.getId() != null) {
            throw new BadRequestAlertException("A new shopNew cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopNew result = shopNewRepository.save(shopNew);
        return ResponseEntity.created(new URI("/api/shop-news/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shop-news} : Updates an existing shopNew.
     *
     * @param shopNew the shopNew to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopNew,
     * or with status {@code 400 (Bad Request)} if the shopNew is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopNew couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shop-news")
    public ResponseEntity<ShopNew> updateShopNew(@Valid @RequestBody ShopNew shopNew) throws URISyntaxException {
        log.debug("REST request to update ShopNew : {}", shopNew);
        if (shopNew.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShopNew result = shopNewRepository.save(shopNew);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shopNew.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shop-news} : get all the shopNews.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopNews in body.
     */
    @GetMapping("/shop-news")
    public ResponseEntity<List<ShopNew>> getAllShopNews(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of ShopNews");
        Page<ShopNew> page;
        if (eagerload) {
            page = shopNewRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = shopNewRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shop-news/:id} : get the "id" shopNew.
     *
     * @param id the id of the shopNew to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopNew, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shop-news/{id}")
    public ResponseEntity<ShopNew> getShopNew(@PathVariable Long id) {
        log.debug("REST request to get ShopNew : {}", id);
        Optional<ShopNew> shopNew = shopNewRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(shopNew);
    }

    /**
     * {@code DELETE  /shop-news/:id} : delete the "id" shopNew.
     *
     * @param id the id of the shopNew to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shop-news/{id}")
    public ResponseEntity<Void> deleteShopNew(@PathVariable Long id) {
        log.debug("REST request to delete ShopNew : {}", id);
        shopNewRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
