package com.wlminus.web.rest;

import com.wlminus.domain.*;
import com.wlminus.domain.enumeration.ConfigKey;
import com.wlminus.repository.*;
import com.wlminus.service.ProductService;
import com.wlminus.service.dto.CartDTO;
import com.wlminus.service.dto.ProductInCartDTO;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.*;

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

    private final CustomerRepository customerRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ShopOrderRepository shopOrderRepository;
    private final OrderDescRepository orderDescRepository;

    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    public FrontResource(CategoryRepository categoryRepository, ProductService productService, ProductRepository productRepository, AppConstRepository appConstRepository, MediaRepository mediaRepository, CustomerRepository customerRepository, ProductSizeRepository productSizeRepository, ShopOrderRepository shopOrderRepository, OrderDescRepository orderDescRepository, ProvinceRepository provinceRepository, DistrictRepository districtRepository, WardRepository wardRepository) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.appConstRepository = appConstRepository;
        this.mediaRepository = mediaRepository;
        this.customerRepository = customerRepository;
        this.productSizeRepository = productSizeRepository;
        this.shopOrderRepository = shopOrderRepository;
        this.orderDescRepository = orderDescRepository;
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

    @PostMapping("/order")
    public ResponseEntity<String> createProduct(@Valid @RequestBody CartDTO cart) {
        log.debug("REST request to create order : {}", cart);

        ShopOrder newOrder = new ShopOrder();
        newOrder.setProvince(cart.getProvince());
        newOrder.setDistrict(cart.getDistrict());
        newOrder.setWard(cart.getWard());

        newOrder.setOrderStatus("1");
        //ShipType
        newOrder.setCreatedBy(cart.getCustomerAddress());
        newOrder.setModifiedBy(cart.getCustomerNote());
        newOrder.setModifiedDate(cart.getShipType());

        newOrder.setCreatedDate(System.currentTimeMillis());

        Set<OrderDesc> orderDescSet = new HashSet<>();
        double totalPrice = 0D;
        //Calculate total price
        for (ProductInCartDTO currentProduct: cart.getOrderList()) {
            Optional<Product> productToGetPrice = productRepository.findById(currentProduct.getProduct().getId());
            if (productToGetPrice.isPresent()) {
                OrderDesc orderDesc = new OrderDesc();

                orderDesc.setProduct(productToGetPrice.get());
                orderDesc.setCount(currentProduct.getAmount());

                Optional<ProductSize> size = productSizeRepository.findBySizeName(currentProduct.getSize());
                if (size.isPresent()) {
                    Double sizeId = (double)size.get().getId();
                    orderDesc.setOrderPrice(sizeId);
                } else {
                    orderDesc.setOrderPrice(-1D);
                }
                double currentPrice = (double) (productToGetPrice.get().getFinalPrice() * currentProduct.getAmount());
                orderDesc.setFinalPrice(currentPrice);

                totalPrice += currentPrice;
                orderDescSet.add(orderDesc);
            }
        }
        newOrder.setOrderDescs(orderDescSet);
        newOrder.setTotalPrice(totalPrice);

        Optional<Customer> isOld = customerRepository.findCustomerByCustomerNameAndAndTel(cart.getCustomerName(), cart.getCustomerPhone());
        if (isOld.isPresent()) {
            newOrder.setCustomer(isOld.get());
        } else {
            Customer newCustomer = new Customer();
            newCustomer.setCustomerName(cart.getCustomerName());
            newCustomer.setTel(cart.getCustomerPhone());

            newCustomer.setProvince(cart.getProvince());
            newCustomer.setDistrict(cart.getDistrict());
            newCustomer.setWard(cart.getWard());

            Customer savedCustomer = customerRepository.save(newCustomer);
            newOrder.setCustomer(savedCustomer);
        }

        ShopOrder saved = shopOrderRepository.save(newOrder);
        for (OrderDesc orderDesc: orderDescSet) {
            orderDesc.setShopOrder(saved);
        }
        orderDescRepository.saveAll(orderDescSet);
        return ResponseEntity.ok("Tiếp nhận order thành công!");
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

    @GetMapping("/config/product/hot")
    public ResponseEntity<List<Product>> getNewProduct() {
        log.debug("FRONT. REST request to get hot product by config list :");
        return ResponseEntity.ok().body(productRepository.findAllByIsValidIsTrue());
    }

    @GetMapping("/related-product")
    public ResponseEntity<List<Product>> getRelatedProduct() {
        log.debug("FRONT. REST request to get hot product by config list :");
        long currentCount = productRepository.count();
        if (currentCount <= 4) {
            return ResponseEntity.ok().body(productRepository.findAllData());
        }
        final long[] randomSeedArr = new Random().longs(1, currentCount).distinct().limit(4).toArray();

        List<Product> allDataProduct = productRepository.findAllData();
        List<Product> random4Data = new ArrayList<>();
        for (long index: randomSeedArr) {
            int indexInInt = ((int) index);
            random4Data.add(allDataProduct.get(indexInInt));
        }

        return ResponseEntity.ok().body(random4Data);
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
