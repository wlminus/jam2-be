package com.wlminus.web.rest;

import com.wlminus.domain.ShopOrder;
import com.wlminus.repository.ShopOrderRepository;
import com.wlminus.security.SecurityUtils;
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

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(ShopOrderResource.class);

    private static final String ENTITY_NAME = "shopOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopOrderRepository shopOrderRepository;

    public ShopOrderResource(ShopOrderRepository shopOrderRepository) {
        this.shopOrderRepository = shopOrderRepository;
    }

//    @PostMapping("/shop-orders")
//    public ResponseEntity<ShopOrder> createShopOrder(@Valid @RequestBody ShopOrder shopOrder) throws URISyntaxException {
//        log.debug("REST request to save ShopOrder : {}", shopOrder);
//        if (shopOrder.getId() != null) {
//            throw new BadRequestAlertException("A new shopOrder cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        ShopOrder result = shopOrderRepository.save(shopOrder);
//        return ResponseEntity.created(new URI("/api/shop-orders/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }

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

    @GetMapping("/shop-orders")
    public ResponseEntity<List<ShopOrder>> getAllShopOrders(Pageable pageable) {
        log.debug("REST request to get a page of ShopOrders");
        Page<ShopOrder> page = shopOrderRepository.findAllOrder(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    //Status
    //1 = order
    //2 = Process
    //3 = cancel
    @GetMapping("/shop-orders/s/{status}")
    public ResponseEntity<List<ShopOrder>> getOrdersByStatus(@PathVariable Integer status, Pageable pageable) {
        log.debug("REST request to get a page of ShopOrders");
        Page<ShopOrder> page = shopOrderRepository.findWithStatus(pageable, status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/shop-orders/{id}")
    public ResponseEntity<ShopOrder> getShopOrder(@PathVariable Long id) {
        log.debug("REST request to get ShopOrder : {}", id);
        Optional<ShopOrder> shopOrder = shopOrderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shopOrder);
    }

    @PutMapping("/shop-orders/process/{id}")
    public ResponseEntity<ShopOrder> processOrder(@PathVariable Long id) {
        log.debug("REST request to get ShopOrder : {}", id);
        Optional<ShopOrder> shopOrder = shopOrderRepository.findById(id);
        if (shopOrder.isPresent()) {
            ShopOrder realData = shopOrder.get();
            realData.setOrderStatus(2);
            realData.setProcessDate(System.currentTimeMillis());
            String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
            realData.setProcessBy(userLogin);

            return ResponseEntity.ok(shopOrderRepository.save(realData));
        }
        return ResponseUtil.wrapOrNotFound(shopOrder);
    }

    @PutMapping("/shop-orders/cancel/{id}")
    public ResponseEntity<ShopOrder> cancelOrder(@PathVariable Long id) {
        log.debug("REST request to cancel ShopOrder : {}", id);
        Optional<ShopOrder> shopOrder = shopOrderRepository.findById(id);
        if (shopOrder.isPresent()) {
            ShopOrder realData = shopOrder.get();
            realData.setOrderStatus(3);
            realData.setProcessDate(System.currentTimeMillis());
            String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
            realData.setProcessBy(userLogin);

            return ResponseEntity.ok(shopOrderRepository.save(realData));
        }
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
