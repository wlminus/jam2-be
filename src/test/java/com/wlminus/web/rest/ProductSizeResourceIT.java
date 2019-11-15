package com.wlminus.web.rest;

import com.wlminus.JamilaApp;
import com.wlminus.domain.ProductSize;
import com.wlminus.repository.ProductSizeRepository;
import com.wlminus.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.wlminus.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductSizeResource} REST controller.
 */
@SpringBootTest(classes = JamilaApp.class)
public class ProductSizeResourceIT {

    private static final String DEFAULT_SIZE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SIZE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ProductSizeRepository productSizeRepository;

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

    private MockMvc restProductSizeMockMvc;

    private ProductSize productSize;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductSizeResource productSizeResource = new ProductSizeResource(productSizeRepository);
        this.restProductSizeMockMvc = MockMvcBuilders.standaloneSetup(productSizeResource)
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
    public static ProductSize createEntity(EntityManager em) {
        ProductSize productSize = new ProductSize()
            .sizeName(DEFAULT_SIZE_NAME)
            .description(DEFAULT_DESCRIPTION);
        return productSize;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductSize createUpdatedEntity(EntityManager em) {
        ProductSize productSize = new ProductSize()
            .sizeName(UPDATED_SIZE_NAME)
            .description(UPDATED_DESCRIPTION);
        return productSize;
    }

    @BeforeEach
    public void initTest() {
        productSize = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductSize() throws Exception {
        int databaseSizeBeforeCreate = productSizeRepository.findAll().size();

        // Create the ProductSize
        restProductSizeMockMvc.perform(post("/api/product-sizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSize)))
            .andExpect(status().isCreated());

        // Validate the ProductSize in the database
        List<ProductSize> productSizeList = productSizeRepository.findAll();
        assertThat(productSizeList).hasSize(databaseSizeBeforeCreate + 1);
        ProductSize testProductSize = productSizeList.get(productSizeList.size() - 1);
        assertThat(testProductSize.getSizeName()).isEqualTo(DEFAULT_SIZE_NAME);
        assertThat(testProductSize.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createProductSizeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productSizeRepository.findAll().size();

        // Create the ProductSize with an existing ID
        productSize.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductSizeMockMvc.perform(post("/api/product-sizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSize)))
            .andExpect(status().isBadRequest());

        // Validate the ProductSize in the database
        List<ProductSize> productSizeList = productSizeRepository.findAll();
        assertThat(productSizeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProductSizes() throws Exception {
        // Initialize the database
        productSizeRepository.saveAndFlush(productSize);

        // Get all the productSizeList
        restProductSizeMockMvc.perform(get("/api/product-sizes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productSize.getId().intValue())))
            .andExpect(jsonPath("$.[*].sizeName").value(hasItem(DEFAULT_SIZE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getProductSize() throws Exception {
        // Initialize the database
        productSizeRepository.saveAndFlush(productSize);

        // Get the productSize
        restProductSizeMockMvc.perform(get("/api/product-sizes/{id}", productSize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productSize.getId().intValue()))
            .andExpect(jsonPath("$.sizeName").value(DEFAULT_SIZE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingProductSize() throws Exception {
        // Get the productSize
        restProductSizeMockMvc.perform(get("/api/product-sizes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductSize() throws Exception {
        // Initialize the database
        productSizeRepository.saveAndFlush(productSize);

        int databaseSizeBeforeUpdate = productSizeRepository.findAll().size();

        // Update the productSize
        ProductSize updatedProductSize = productSizeRepository.findById(productSize.getId()).get();
        // Disconnect from session so that the updates on updatedProductSize are not directly saved in db
        em.detach(updatedProductSize);
        updatedProductSize
            .sizeName(UPDATED_SIZE_NAME)
            .description(UPDATED_DESCRIPTION);

        restProductSizeMockMvc.perform(put("/api/product-sizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductSize)))
            .andExpect(status().isOk());

        // Validate the ProductSize in the database
        List<ProductSize> productSizeList = productSizeRepository.findAll();
        assertThat(productSizeList).hasSize(databaseSizeBeforeUpdate);
        ProductSize testProductSize = productSizeList.get(productSizeList.size() - 1);
        assertThat(testProductSize.getSizeName()).isEqualTo(UPDATED_SIZE_NAME);
        assertThat(testProductSize.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingProductSize() throws Exception {
        int databaseSizeBeforeUpdate = productSizeRepository.findAll().size();

        // Create the ProductSize

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductSizeMockMvc.perform(put("/api/product-sizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSize)))
            .andExpect(status().isBadRequest());

        // Validate the ProductSize in the database
        List<ProductSize> productSizeList = productSizeRepository.findAll();
        assertThat(productSizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductSize() throws Exception {
        // Initialize the database
        productSizeRepository.saveAndFlush(productSize);

        int databaseSizeBeforeDelete = productSizeRepository.findAll().size();

        // Delete the productSize
        restProductSizeMockMvc.perform(delete("/api/product-sizes/{id}", productSize.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductSize> productSizeList = productSizeRepository.findAll();
        assertThat(productSizeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
