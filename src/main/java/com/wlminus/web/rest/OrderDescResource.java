package com.wlminus.web.rest;

import com.wlminus.domain.OrderDesc;
import com.wlminus.repository.OrderDescRepository;
import com.wlminus.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.wlminus.domain.OrderDesc}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrderDescResource {

    private final Logger log = LoggerFactory.getLogger(OrderDescResource.class);

    private static final String ENTITY_NAME = "orderDesc";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderDescRepository orderDescRepository;

    public OrderDescResource(OrderDescRepository orderDescRepository) {
        this.orderDescRepository = orderDescRepository;
    }

    /**
     * {@code POST  /order-descs} : Create a new orderDesc.
     *
     * @param orderDesc the orderDesc to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderDesc, or with status {@code 400 (Bad Request)} if the orderDesc has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-descs")
    public ResponseEntity<OrderDesc> createOrderDesc(@Valid @RequestBody OrderDesc orderDesc) throws URISyntaxException {
        log.debug("REST request to save OrderDesc : {}", orderDesc);
        if (orderDesc.getId() != null) {
            throw new BadRequestAlertException("A new orderDesc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderDesc result = orderDescRepository.save(orderDesc);
        return ResponseEntity.created(new URI("/api/order-descs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-descs} : Updates an existing orderDesc.
     *
     * @param orderDesc the orderDesc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderDesc,
     * or with status {@code 400 (Bad Request)} if the orderDesc is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderDesc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-descs")
    public ResponseEntity<OrderDesc> updateOrderDesc(@Valid @RequestBody OrderDesc orderDesc) throws URISyntaxException {
        log.debug("REST request to update OrderDesc : {}", orderDesc);
        if (orderDesc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderDesc result = orderDescRepository.save(orderDesc);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderDesc.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /order-descs} : get all the orderDescs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderDescs in body.
     */
    @GetMapping("/order-descs")
    public List<OrderDesc> getAllOrderDescs() {
        log.debug("REST request to get all OrderDescs");
        return orderDescRepository.findAll();
    }

    /**
     * {@code GET  /order-descs/:id} : get the "id" orderDesc.
     *
     * @param id the id of the orderDesc to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderDesc, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-descs/{id}")
    public ResponseEntity<OrderDesc> getOrderDesc(@PathVariable Long id) {
        log.debug("REST request to get OrderDesc : {}", id);
        Optional<OrderDesc> orderDesc = orderDescRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orderDesc);
    }

    /**
     * {@code DELETE  /order-descs/:id} : delete the "id" orderDesc.
     *
     * @param id the id of the orderDesc to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-descs/{id}")
    public ResponseEntity<Void> deleteOrderDesc(@PathVariable Long id) {
        log.debug("REST request to delete OrderDesc : {}", id);
        orderDescRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
