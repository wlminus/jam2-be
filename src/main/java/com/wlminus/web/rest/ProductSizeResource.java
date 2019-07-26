package com.wlminus.web.rest;

import com.wlminus.domain.ProductSize;
import com.wlminus.repository.ProductSizeRepository;
import com.wlminus.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.wlminus.domain.ProductSize}.
 */
@RestController
@RequestMapping("/api")
public class ProductSizeResource {

    private final Logger log = LoggerFactory.getLogger(ProductSizeResource.class);

    private static final String ENTITY_NAME = "productSize";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductSizeRepository productSizeRepository;

    public ProductSizeResource(ProductSizeRepository productSizeRepository) {
        this.productSizeRepository = productSizeRepository;
    }

    /**
     * {@code POST  /product-sizes} : Create a new productSize.
     *
     * @param productSize the productSize to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productSize, or with status {@code 400 (Bad Request)} if the productSize has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-sizes")
    public ResponseEntity<ProductSize> createProductSize(@Valid @RequestBody ProductSize productSize) throws URISyntaxException {
        log.debug("REST request to save ProductSize : {}", productSize);
        if (productSize.getId() != null) {
            throw new BadRequestAlertException("A new productSize cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductSize result = productSizeRepository.save(productSize);
        return ResponseEntity.created(new URI("/api/product-sizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-sizes} : Updates an existing productSize.
     *
     * @param productSize the productSize to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productSize,
     * or with status {@code 400 (Bad Request)} if the productSize is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productSize couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-sizes")
    public ResponseEntity<ProductSize> updateProductSize(@Valid @RequestBody ProductSize productSize) throws URISyntaxException {
        log.debug("REST request to update ProductSize : {}", productSize);
        if (productSize.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductSize result = productSizeRepository.save(productSize);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productSize.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-sizes} : get all the productSizes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productSizes in body.
     */
    @GetMapping("/product-sizes")
    public List<ProductSize> getAllProductSizes() {
        log.debug("REST request to get all ProductSizes");
        return productSizeRepository.findAll();
    }

    /**
     * {@code GET  /product-sizes/:id} : get the "id" productSize.
     *
     * @param id the id of the productSize to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productSize, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-sizes/{id}")
    public ResponseEntity<ProductSize> getProductSize(@PathVariable Long id) {
        log.debug("REST request to get ProductSize : {}", id);
        Optional<ProductSize> productSize = productSizeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productSize);
    }

    /**
     * {@code DELETE  /product-sizes/:id} : delete the "id" productSize.
     *
     * @param id the id of the productSize to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-sizes/{id}")
    public ResponseEntity<Void> deleteProductSize(@PathVariable Long id) {
        log.debug("REST request to delete ProductSize : {}", id);
        productSizeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
