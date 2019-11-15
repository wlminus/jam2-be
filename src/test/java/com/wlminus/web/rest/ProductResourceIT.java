package com.wlminus.web.rest;

import com.wlminus.JamilaApp;
import com.wlminus.domain.Product;
import com.wlminus.repository.ProductRepository;
import com.wlminus.service.ProductService;
import com.wlminus.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.wlminus.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@SpringBootTest(classes = JamilaApp.class)
public class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;

    private static final Long DEFAULT_FINAL_PRICE = 1L;
    private static final Long UPDATED_FINAL_PRICE = 2L;

    private static final Double DEFAULT_DISCOUNT = 0D;
    private static final Double UPDATED_DISCOUNT = 1D;

    private static final String DEFAULT_RELEASE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MATERIAL_DESC = "AAAAAAAAAA";
    private static final String UPDATED_MATERIAL_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_VALID = false;
    private static final Boolean UPDATED_IS_VALID = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_DATE = 1L;
    private static final Long UPDATED_CREATED_DATE = 2L;

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_MODIFIED_DATE = 1L;
    private static final Long UPDATED_MODIFIED_DATE = 2L;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private ProductService productService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restProductMockMvc;

    private Product product;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productService);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .productCode(DEFAULT_PRODUCT_CODE)
            .price(DEFAULT_PRICE)
            .finalPrice(DEFAULT_FINAL_PRICE)
            .discount(DEFAULT_DISCOUNT)
            .releaseType(DEFAULT_RELEASE_TYPE)
            .releaseStatus(DEFAULT_RELEASE_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .materialDesc(DEFAULT_MATERIAL_DESC)
            .slug(DEFAULT_SLUG)
            .isValid(DEFAULT_IS_VALID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return product;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .name(UPDATED_NAME)
            .productCode(UPDATED_PRODUCT_CODE)
            .price(UPDATED_PRICE)
            .finalPrice(UPDATED_FINAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .releaseType(UPDATED_RELEASE_TYPE)
            .releaseStatus(UPDATED_RELEASE_STATUS)
            .description(UPDATED_DESCRIPTION)
            .materialDesc(UPDATED_MATERIAL_DESC)
            .slug(UPDATED_SLUG)
            .isValid(UPDATED_IS_VALID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getFinalPrice()).isEqualTo(DEFAULT_FINAL_PRICE);
        assertThat(testProduct.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testProduct.getReleaseType()).isEqualTo(DEFAULT_RELEASE_TYPE);
        assertThat(testProduct.getReleaseStatus()).isEqualTo(DEFAULT_RELEASE_STATUS);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getMaterialDesc()).isEqualTo(DEFAULT_MATERIAL_DESC);
        assertThat(testProduct.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testProduct.isIsValid()).isEqualTo(DEFAULT_IS_VALID);
        assertThat(testProduct.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProduct.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProduct.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testProduct.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setPrice(null);

        // Create the Product, which fails.

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setFinalPrice(null);

        // Create the Product, which fails.

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].finalPrice").value(hasItem(DEFAULT_FINAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].releaseType").value(hasItem(DEFAULT_RELEASE_TYPE)))
            .andExpect(jsonPath("$.[*].releaseStatus").value(hasItem(DEFAULT_RELEASE_STATUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].materialDesc").value(hasItem(DEFAULT_MATERIAL_DESC)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].isValid").value(hasItem(DEFAULT_IS_VALID.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.intValue())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.intValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        ProductResource productResource = new ProductResource(productServiceMock);
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProductMockMvc.perform(get("/api/products?eagerload=true"))
        .andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ProductResource productResource = new ProductResource(productServiceMock);
            when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProductMockMvc.perform(get("/api/products?eagerload=true"))
        .andExpect(status().isOk());

            verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.finalPrice").value(DEFAULT_FINAL_PRICE.intValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.releaseType").value(DEFAULT_RELEASE_TYPE))
            .andExpect(jsonPath("$.releaseStatus").value(DEFAULT_RELEASE_STATUS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.materialDesc").value(DEFAULT_MATERIAL_DESC))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.isValid").value(DEFAULT_IS_VALID.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.intValue()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productService.save(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .productCode(UPDATED_PRODUCT_CODE)
            .price(UPDATED_PRICE)
            .finalPrice(UPDATED_FINAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .releaseType(UPDATED_RELEASE_TYPE)
            .releaseStatus(UPDATED_RELEASE_STATUS)
            .description(UPDATED_DESCRIPTION)
            .materialDesc(UPDATED_MATERIAL_DESC)
            .slug(UPDATED_SLUG)
            .isValid(UPDATED_IS_VALID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProduct)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
        assertThat(testProduct.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testProduct.getReleaseType()).isEqualTo(UPDATED_RELEASE_TYPE);
        assertThat(testProduct.getReleaseStatus()).isEqualTo(UPDATED_RELEASE_STATUS);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getMaterialDesc()).isEqualTo(UPDATED_MATERIAL_DESC);
        assertThat(testProduct.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testProduct.isIsValid()).isEqualTo(UPDATED_IS_VALID);
        assertThat(testProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProduct.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProduct.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testProduct.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productService.save(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
