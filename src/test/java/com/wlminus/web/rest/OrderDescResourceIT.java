package com.wlminus.web.rest;

import com.wlminus.JamilaApp;
import com.wlminus.domain.OrderDesc;
import com.wlminus.repository.OrderDescRepository;
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
 * Integration tests for the {@link OrderDescResource} REST controller.
 */
@SpringBootTest(classes = JamilaApp.class)
public class OrderDescResourceIT {

    private static final Long DEFAULT_COUNT = 1L;
    private static final Long UPDATED_COUNT = 2L;

    private static final Double DEFAULT_ORDER_PRICE = 0D;
    private static final Double UPDATED_ORDER_PRICE = 1D;

    private static final Double DEFAULT_FINAL_PRICE = 0D;
    private static final Double UPDATED_FINAL_PRICE = 1D;

    @Autowired
    private OrderDescRepository orderDescRepository;

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

    private MockMvc restOrderDescMockMvc;

    private OrderDesc orderDesc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderDescResource orderDescResource = new OrderDescResource(orderDescRepository);
        this.restOrderDescMockMvc = MockMvcBuilders.standaloneSetup(orderDescResource)
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
    public static OrderDesc createEntity(EntityManager em) {
        OrderDesc orderDesc = new OrderDesc()
            .count(DEFAULT_COUNT)
            .orderPrice(DEFAULT_ORDER_PRICE)
            .finalPrice(DEFAULT_FINAL_PRICE);
        return orderDesc;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderDesc createUpdatedEntity(EntityManager em) {
        OrderDesc orderDesc = new OrderDesc()
            .count(UPDATED_COUNT)
            .orderPrice(UPDATED_ORDER_PRICE)
            .finalPrice(UPDATED_FINAL_PRICE);
        return orderDesc;
    }

    @BeforeEach
    public void initTest() {
        orderDesc = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderDesc() throws Exception {
        int databaseSizeBeforeCreate = orderDescRepository.findAll().size();

        // Create the OrderDesc
        restOrderDescMockMvc.perform(post("/api/order-descs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDesc)))
            .andExpect(status().isCreated());

        // Validate the OrderDesc in the database
        List<OrderDesc> orderDescList = orderDescRepository.findAll();
        assertThat(orderDescList).hasSize(databaseSizeBeforeCreate + 1);
        OrderDesc testOrderDesc = orderDescList.get(orderDescList.size() - 1);
        assertThat(testOrderDesc.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testOrderDesc.getOrderPrice()).isEqualTo(DEFAULT_ORDER_PRICE);
        assertThat(testOrderDesc.getFinalPrice()).isEqualTo(DEFAULT_FINAL_PRICE);
    }

    @Test
    @Transactional
    public void createOrderDescWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderDescRepository.findAll().size();

        // Create the OrderDesc with an existing ID
        orderDesc.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderDescMockMvc.perform(post("/api/order-descs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDesc)))
            .andExpect(status().isBadRequest());

        // Validate the OrderDesc in the database
        List<OrderDesc> orderDescList = orderDescRepository.findAll();
        assertThat(orderDescList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDescRepository.findAll().size();
        // set the field null
        orderDesc.setCount(null);

        // Create the OrderDesc, which fails.

        restOrderDescMockMvc.perform(post("/api/order-descs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDesc)))
            .andExpect(status().isBadRequest());

        List<OrderDesc> orderDescList = orderDescRepository.findAll();
        assertThat(orderDescList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDescRepository.findAll().size();
        // set the field null
        orderDesc.setOrderPrice(null);

        // Create the OrderDesc, which fails.

        restOrderDescMockMvc.perform(post("/api/order-descs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDesc)))
            .andExpect(status().isBadRequest());

        List<OrderDesc> orderDescList = orderDescRepository.findAll();
        assertThat(orderDescList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDescRepository.findAll().size();
        // set the field null
        orderDesc.setFinalPrice(null);

        // Create the OrderDesc, which fails.

        restOrderDescMockMvc.perform(post("/api/order-descs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDesc)))
            .andExpect(status().isBadRequest());

        List<OrderDesc> orderDescList = orderDescRepository.findAll();
        assertThat(orderDescList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderDescs() throws Exception {
        // Initialize the database
        orderDescRepository.saveAndFlush(orderDesc);

        // Get all the orderDescList
        restOrderDescMockMvc.perform(get("/api/order-descs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDesc.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].orderPrice").value(hasItem(DEFAULT_ORDER_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].finalPrice").value(hasItem(DEFAULT_FINAL_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getOrderDesc() throws Exception {
        // Initialize the database
        orderDescRepository.saveAndFlush(orderDesc);

        // Get the orderDesc
        restOrderDescMockMvc.perform(get("/api/order-descs/{id}", orderDesc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderDesc.getId().intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()))
            .andExpect(jsonPath("$.orderPrice").value(DEFAULT_ORDER_PRICE.doubleValue()))
            .andExpect(jsonPath("$.finalPrice").value(DEFAULT_FINAL_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderDesc() throws Exception {
        // Get the orderDesc
        restOrderDescMockMvc.perform(get("/api/order-descs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderDesc() throws Exception {
        // Initialize the database
        orderDescRepository.saveAndFlush(orderDesc);

        int databaseSizeBeforeUpdate = orderDescRepository.findAll().size();

        // Update the orderDesc
        OrderDesc updatedOrderDesc = orderDescRepository.findById(orderDesc.getId()).get();
        // Disconnect from session so that the updates on updatedOrderDesc are not directly saved in db
        em.detach(updatedOrderDesc);
        updatedOrderDesc
            .count(UPDATED_COUNT)
            .orderPrice(UPDATED_ORDER_PRICE)
            .finalPrice(UPDATED_FINAL_PRICE);

        restOrderDescMockMvc.perform(put("/api/order-descs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrderDesc)))
            .andExpect(status().isOk());

        // Validate the OrderDesc in the database
        List<OrderDesc> orderDescList = orderDescRepository.findAll();
        assertThat(orderDescList).hasSize(databaseSizeBeforeUpdate);
        OrderDesc testOrderDesc = orderDescList.get(orderDescList.size() - 1);
        assertThat(testOrderDesc.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testOrderDesc.getOrderPrice()).isEqualTo(UPDATED_ORDER_PRICE);
        assertThat(testOrderDesc.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderDesc() throws Exception {
        int databaseSizeBeforeUpdate = orderDescRepository.findAll().size();

        // Create the OrderDesc

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderDescMockMvc.perform(put("/api/order-descs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDesc)))
            .andExpect(status().isBadRequest());

        // Validate the OrderDesc in the database
        List<OrderDesc> orderDescList = orderDescRepository.findAll();
        assertThat(orderDescList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrderDesc() throws Exception {
        // Initialize the database
        orderDescRepository.saveAndFlush(orderDesc);

        int databaseSizeBeforeDelete = orderDescRepository.findAll().size();

        // Delete the orderDesc
        restOrderDescMockMvc.perform(delete("/api/order-descs/{id}", orderDesc.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderDesc> orderDescList = orderDescRepository.findAll();
        assertThat(orderDescList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
