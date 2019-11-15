package com.wlminus.web.rest;

import com.wlminus.domain.ShopOrder;
import com.wlminus.repository.ShopOrderRepository;
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
 * REST controller for managing {@link com.wlminus.domain.ShopOrder}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ShopOrderResource {

    private final Logger log = LoggerFactory.getLogger(ShopOrderResource.class);

    private static final String ENTITY_NAME = "shopOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopOrderRepository shopOrderRepository;

    public ShopOrderResource(ShopOrderRepository shopOrderRepository) {
        this.shopOrderRepository = shopOrderRepository;
    }

    /**
     * {@code POST  /shop-orders} : Create a new shopOrder.
     *
     * @param shopOrder the shopOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopOrder, or with status {@code 400 (Bad Request)} if the shopOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shop-orders")
    public ResponseEntity<ShopOrder> createShopOrder(@Valid @RequestBody ShopOrder shopOrder) throws URISyntaxException {
        log.debug("REST request to save ShopOrder : {}", shopOrder);
        if (shopOrder.getId() != null) {
            throw new BadRequestAlertException("A new shopOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopOrder result = shopOrderRepository.save(shopOrder);
        return ResponseEntity.created(new URI("/api/shop-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shop-orders} : Updates an existing shopOrder.
     *
     * @param shopOrder the shopOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopOrder,
     * or with status {@code 400 (Bad Request)} if the shopOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shop-orders")
    public ResponseEntity<ShopOrder> updateShopOrder(@Valid @RequestBody ShopOrder shopOrder) throws URISyntaxException {
        log.debug("REST request to update ShopOrder : {}", shopOrder);
        if (shopOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShopOrder result = shopOrderRepository.save(shopOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shopOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shop-orders} : get all the shopOrders.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopOrders in body.
     */
    @GetMapping("/shop-orders")
    public ResponseEntity<List<ShopOrder>> getAllShopOrders(Pageable pageable) {
        log.debug("REST request to get a page of ShopOrders");
        Page<ShopOrder> page = shopOrderRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shop-orders/:id} : get the "id" shopOrder.
     *
     * @param id the id of the shopOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shop-orders/{id}")
    public ResponseEntity<ShopOrder> getShopOrder(@PathVariable Long id) {
        log.debug("REST request to get ShopOrder : {}", id);
        Optional<ShopOrder> shopOrder = shopOrderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shopOrder);
    }

    /**
     * {@code DELETE  /shop-orders/:id} : delete the "id" shopOrder.
     *
     * @param id the id of the shopOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shop-orders/{id}")
    public ResponseEntity<Void> deleteShopOrder(@PathVariable Long id) {
        log.debug("REST request to delete ShopOrder : {}", id);
        shopOrderRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
