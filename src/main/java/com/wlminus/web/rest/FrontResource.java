package com.wlminus.web.rest;
import com.wlminus.domain.*;
import com.wlminus.repository.CategoryRepository;
import com.wlminus.repository.ProductRepository;
import com.wlminus.service.ProductService;
import com.wlminus.service.dto.CartDTO;
import com.wlminus.service.dto.ProductInCartDTO;
import com.wlminus.web.rest.errors.BadRequestAlertException;
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
import org.terracotta.context.annotations.ContextAttribute;

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
    public ResponseEntity<List<Product>> getProductByCategorySlug(@PathVariable String slug, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("FRONT. REST request to get all product by category slug: {}", slug);
        Page<Product> page = productRepository.findAllByCategorySlug(slug, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/products/id/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.debug("FRONT. REST request to get Product by id : {}", id);
        Optional<Product> product = productService.findOne(id);
        return ResponseUtil.wrapOrNotFound(product);
    }

    @GetMapping("/products/slug/{slug}")
    public ResponseEntity<Product> getProductBySlug(@PathVariable String slug) {
        log.debug("FRONT. REST request to get Product by slug : {}", slug);
        Optional<Product> product = productRepository.findOneBySlug(slug);
        return ResponseUtil.wrapOrNotFound(product);
    }

    @GetMapping("/products/search/{query}")
    public ResponseEntity<List<Product>> searchProduct(@PathVariable String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("FRONT. REST request to search product by query: {}", query);
        Page<Product> page = productRepository.searchProduct(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/card")
    public ResponseEntity<ShopOrder> createProduct(@Valid @RequestBody CartDTO cart) throws URISyntaxException {
        log.debug("REST request to create order : {}", cart);
        if (cart.getId() != null) {
            throw new BadRequestAlertException("A new order cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Customer customer = new Customer();
        customer.setCustomerName(cart.getCustomerName());
        customer.setTel(cart.getMobilePhone());

        customer.setProvince(cart.getProvince());
        customer.setDistrict(cart.getDistrict());
        customer.setWard(cart.getWard());

        for (ProductInCartDTO desc: cart.getListCard()) {
            OrderDesc tmp = new OrderDesc();

        }
//        Product result = productService.save(product);
//        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//            .body(result);
    }
}
