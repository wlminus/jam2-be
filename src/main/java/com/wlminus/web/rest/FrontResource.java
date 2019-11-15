package com.wlminus.web.rest;

import com.wlminus.domain.*;
import com.wlminus.domain.enumeration.ConfigKey;
import com.wlminus.repository.*;
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

    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    public FrontResource(CategoryRepository categoryRepository, ProductService productService, ProductRepository productRepository, AppConstRepository appConstRepository, MediaRepository mediaRepository, ProvinceRepository provinceRepository, DistrictRepository districtRepository, WardRepository wardRepository) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.appConstRepository = appConstRepository;
        this.mediaRepository = mediaRepository;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.wardRepository = wardRepository;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        log.debug("FRONT. REST request to get all Categories");
        return ResponseEntity.ok().body(categoryRepository.findAll());
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

    @GetMapping("/config/media")
    public ResponseEntity<List<Media>> getMediaByConfig() {
        log.debug("FRONT. REST request to get media by config list :");
        try {
            List<AppConst> appConstList = appConstRepository.findAll();
            List<Media> dataReturn = new ArrayList<>();
            Integer count = 0;
            for (AppConst item : appConstList) {
                if (item.getConstKey() == ConfigKey.HOME_SLIDE_LIST) {
                    String[] listMediaId = item.getConstValue().split(",");
                    count = listMediaId.length;
                    for (String id : listMediaId) {
                        Optional<Media> tmp = mediaRepository.findById(Long.parseLong(id));
                        if (tmp.isPresent()) {
                            dataReturn.add(tmp.get());
                        }
                    }
                }
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", count.toString());
            return ResponseEntity.ok().headers(headers).body(dataReturn);
        } catch (Exception ex) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", "0");
            return ResponseEntity.ok().headers(headers).body(null);
        }
    }

    @GetMapping("/config/product/new")
    public ResponseEntity<List<Product>> getNewProduct() {
        log.debug("FRONT. REST request to get new product by config list :");
        return ResponseEntity.ok().body(productRepository.findAllByIsValidIsTrue());
    }

    @GetMapping("/province")
    public ResponseEntity<List<Province>> getProvince() {
        log.debug("FRONT. REST request get list province");
        return ResponseEntity.ok().body(provinceRepository.findAll());
    }

    @GetMapping("/district/p/{provinceId}")
    public ResponseEntity<List<District>> getProvince(@PathVariable Long provinceId) {
        log.debug("FRONT. REST request get list district by provinceId");
        return ResponseEntity.ok().body(districtRepository.findAllByProvince_Id(provinceId));
    }

    @GetMapping("/ward/d/{districtId}")
    public ResponseEntity<List<Ward>> getWard(@PathVariable Long districtId) {
        log.debug("FRONT. REST request get list district by provinceId");
        return ResponseEntity.ok().body(wardRepository.findAllByDistrictId(districtId));
    }
}
