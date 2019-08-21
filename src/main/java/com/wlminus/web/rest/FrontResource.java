package com.wlminus.web.rest;

import com.wlminus.domain.*;
import com.wlminus.repository.AppConstRepository;
import com.wlminus.repository.CategoryRepository;
import com.wlminus.repository.MediaRepository;
import com.wlminus.repository.ProductRepository;
import com.wlminus.service.ProductService;
import com.wlminus.service.dto.CartDTO;
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

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/front")
public class FrontResource {
    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private static final String ENTITY_NAME = "Front";
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final AppConstRepository appConstRepository;
    private final MediaRepository mediaRepository;

    public FrontResource(CategoryRepository categoryRepository, ProductService productService, ProductRepository productRepository, AppConstRepository appConstRepository, MediaRepository mediaRepository) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.appConstRepository = appConstRepository;
        this.mediaRepository = mediaRepository;
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
        Page<Product> page = productRepository.searchProduct(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/card")
    public void createProduct(@Valid @RequestBody CartDTO cart) throws URISyntaxException {
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

//        for (ProductInCartDTO desc: cart.getListCard()) {
//            OrderDesc tmp = new OrderDesc();
//        }
        return;
    }

//    @GetMapping("/home/key/{configKey}")
//    public ResponseEntity<AppConst> getOneAppConstByKey(@PathVariable String configKey) {
//        log.debug("FRONT. REST request to get config by key : {}", configKey);
//        Optional<AppConst> appConst = appConstRepository.findOneByKey(configKey);
//        return ResponseUtil.wrapOrNotFound(appConst);
//    }

    @GetMapping("/config")
    public ResponseEntity<List<AppConst>> getManyAppConstByKey() {
        log.debug("FRONT. REST request to get config list :");
        List<AppConst> appConst = appConstRepository.findAll();
        return ResponseEntity.ok().body(appConst);
    }

    @GetMapping("/config/media/{mediaList}")
    public ResponseEntity<List<Media>> getMediaByConfig(@PathVariable String mediaList) {
        log.debug("FRONT. REST request to get media by config list :");
        String[] listMediaId = mediaList.split(",");
        List<Media> dataReturn = new ArrayList<>();
        for (String id : listMediaId) {
            Optional<Media> tmp = mediaRepository.findById(Long.parseLong(id));
            if (tmp.isPresent()) {
                dataReturn.add(tmp.get());
            }
        }
        return ResponseEntity.ok().body(dataReturn);
    }
}
