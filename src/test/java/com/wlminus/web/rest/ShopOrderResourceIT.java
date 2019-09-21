package com.wlminus.web.rest;

import com.wlminus.JamilaApp;
import com.wlminus.domain.ShopOrder;
import com.wlminus.repository.ShopOrderRepository;
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
 * Integration tests for the {@link ShopOrderResource} REST controller.
 */
@SpringBootTest(classes = JamilaApp.class)
public class ShopOrderResourceIT {

    private static final String DEFAULT_ORDER_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_STATUS = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_PRICE = 0D;
    private static final Double UPDATED_TOTAL_PRICE = 1D;
    private static final Double SMALLER_TOTAL_PRICE = 0D - 1D;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_DATE = 1L;
    private static final Long UPDATED_CREATED_DATE = 2L;
    private static final Long SMALLER_CREATED_DATE = 1L - 1L;

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_MODIFIED_DATE = 1L;
    private static final Long UPDATED_MODIFIED_DATE = 2L;
    private static final Long SMALLER_MODIFIED_DATE = 1L - 1L;

    @Autowired
    private ShopOrderRepository shopOrderRepository;

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

    private MockMvc restShopOrderMockMvc;

    private ShopOrder shopOrder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopOrderResource shopOrderResource = new ShopOrderResource(shopOrderRepository);
        this.restShopOrderMockMvc = MockMvcBuilders.standaloneSetup(shopOrderResource)
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
    public static ShopOrder createEntity(EntityManager em) {
        ShopOrder shopOrder = new ShopOrder()
            .orderStatus(DEFAULT_ORDER_STATUS)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return shopOrder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopOrder createUpdatedEntity(EntityManager em) {
        ShopOrder shopOrder = new ShopOrder()
            .orderStatus(UPDATED_ORDER_STATUS)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return shopOrder;
    }

    @BeforeEach
    public void initTest() {
        shopOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createShopOrder() throws Exception {
        int databaseSizeBeforeCreate = shopOrderRepository.findAll().size();

        // Create the ShopOrder
        restShopOrderMockMvc.perform(post("/api/shop-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopOrder)))
            .andExpect(status().isCreated());

        // Validate the ShopOrder in the database
        List<ShopOrder> shopOrderList = shopOrderRepository.findAll();
        assertThat(shopOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ShopOrder testShopOrder = shopOrderList.get(shopOrderList.size() - 1);
        assertThat(testShopOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testShopOrder.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testShopOrder.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testShopOrder.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testShopOrder.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testShopOrder.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createShopOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopOrderRepository.findAll().size();

        // Create the ShopOrder with an existing ID
        shopOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopOrderMockMvc.perform(post("/api/shop-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopOrder)))
            .andExpect(status().isBadRequest());

        // Validate the ShopOrder in the database
        List<ShopOrder> shopOrderList = shopOrderRepository.findAll();
        assertThat(shopOrderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopOrderRepository.findAll().size();
        // set the field null
        shopOrder.setTotalPrice(null);

        // Create the ShopOrder, which fails.

        restShopOrderMockMvc.perform(post("/api/shop-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopOrder)))
            .andExpect(status().isBadRequest());

        List<ShopOrder> shopOrderList = shopOrderRepository.findAll();
        assertThat(shopOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShopOrders() throws Exception {
        // Initialize the database
        shopOrderRepository.saveAndFlush(shopOrder);

        // Get all the shopOrderList
        restShopOrderMockMvc.perform(get("/api/shop-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.intValue())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.intValue())));
    }
    
    @Test
    @Transactional
    public void getShopOrder() throws Exception {
        // Initialize the database
        shopOrderRepository.saveAndFlush(shopOrder);

        // Get the shopOrder
        restShopOrderMockMvc.perform(get("/api/shop-orders/{id}", shopOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shopOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.intValue()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingShopOrder() throws Exception {
        // Get the shopOrder
        restShopOrderMockMvc.perform(get("/api/shop-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShopOrder() throws Exception {
        // Initialize the database
        shopOrderRepository.saveAndFlush(shopOrder);

        int databaseSizeBeforeUpdate = shopOrderRepository.findAll().size();

        // Update the shopOrder
        ShopOrder updatedShopOrder = shopOrderRepository.findById(shopOrder.getId()).get();
        // Disconnect from session so that the updates on updatedShopOrder are not directly saved in db
        em.detach(updatedShopOrder);
        updatedShopOrder
            .orderStatus(UPDATED_ORDER_STATUS)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restShopOrderMockMvc.perform(put("/api/shop-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShopOrder)))
            .andExpect(status().isOk());

        // Validate the ShopOrder in the database
        List<ShopOrder> shopOrderList = shopOrderRepository.findAll();
        assertThat(shopOrderList).hasSize(databaseSizeBeforeUpdate);
        ShopOrder testShopOrder = shopOrderList.get(shopOrderList.size() - 1);
        assertThat(testShopOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testShopOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testShopOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testShopOrder.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testShopOrder.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testShopOrder.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingShopOrder() throws Exception {
        int databaseSizeBeforeUpdate = shopOrderRepository.findAll().size();

        // Create the ShopOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopOrderMockMvc.perform(put("/api/shop-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopOrder)))
            .andExpect(status().isBadRequest());

        // Validate the ShopOrder in the database
        List<ShopOrder> shopOrderList = shopOrderRepository.findAll();
        assertThat(shopOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShopOrder() throws Exception {
        // Initialize the database
        shopOrderRepository.saveAndFlush(shopOrder);

        int databaseSizeBeforeDelete = shopOrderRepository.findAll().size();

        // Delete the shopOrder
        restShopOrderMockMvc.perform(delete("/api/shop-orders/{id}", shopOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShopOrder> shopOrderList = shopOrderRepository.findAll();
        assertThat(shopOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopOrder.class);
        ShopOrder shopOrder1 = new ShopOrder();
        shopOrder1.setId(1L);
        ShopOrder shopOrder2 = new ShopOrder();
        shopOrder2.setId(shopOrder1.getId());
        assertThat(shopOrder1).isEqualTo(shopOrder2);
        shopOrder2.setId(2L);
        assertThat(shopOrder1).isNotEqualTo(shopOrder2);
        shopOrder1.setId(null);
        assertThat(shopOrder1).isNotEqualTo(shopOrder2);
    }
}
