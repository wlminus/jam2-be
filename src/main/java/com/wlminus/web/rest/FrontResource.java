package com.wlminus.web.rest;
import com.wlminus.domain.Category;
import com.wlminus.domain.Product;
import com.wlminus.repository.CategoryRepository;
import com.wlminus.repository.ProductRepository;
import com.wlminus.service.ProductService;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/front")
public class FrontResource {
    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private static final String ENTITY_NAME = "Front";
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    public FrontResource(CategoryRepository categoryRepository, ProductService productService, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }


    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        log.debug("FRONT. REST request to get all Categories");
        return categoryRepository.findAll();
    }

    @GetMapping("/products/cat/{slug}")
    public ResponseEntity<List<Product>> getProductByCategorySlug(@PathVariable String slug, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder, @RequestParam(required = false, defaultValue = "true") boolean eagerload) {
        log.debug("FRONT. REST request to get all product by category slug: {}", slug);
        Page<Product> page;
        if (eagerload) {
            page = productService.findAllByCategorySlug(slug, pageable);
        } else {
            page = productService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/products/id/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.debug("FRONT. REST request to get Product by id : {}", id);
        Optional<Product> product = productService.findOne(id);
        return ResponseUtil.wrapOrNotFound(product);
    }

}
