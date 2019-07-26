package com.wlminus.web.rest;

import com.wlminus.JamilaApp;
import com.wlminus.domain.ShopNew;
import com.wlminus.repository.ShopNewRepository;
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
 * Integration tests for the {@Link ShopNewResource} REST controller.
 */
@SpringBootTest(classes = JamilaApp.class)
public class ShopNewResourceIT {

    private static final String DEFAULT_NEW_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_NEW_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_NEW_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_NEW_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_DATE = 1L;
    private static final Long UPDATED_CREATED_DATE = 2L;

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_MODIFIED_DATE = 1L;
    private static final Long UPDATED_MODIFIED_DATE = 2L;

    @Autowired
    private ShopNewRepository shopNewRepository;

    @Mock
    private ShopNewRepository shopNewRepositoryMock;

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

    private MockMvc restShopNewMockMvc;

    private ShopNew shopNew;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopNewResource shopNewResource = new ShopNewResource(shopNewRepository);
        this.restShopNewMockMvc = MockMvcBuilders.standaloneSetup(shopNewResource)
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
    public static ShopNew createEntity(EntityManager em) {
        ShopNew shopNew = new ShopNew()
            .newTitle(DEFAULT_NEW_TITLE)
            .newContent(DEFAULT_NEW_CONTENT)
            .newType(DEFAULT_NEW_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return shopNew;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopNew createUpdatedEntity(EntityManager em) {
        ShopNew shopNew = new ShopNew()
            .newTitle(UPDATED_NEW_TITLE)
            .newContent(UPDATED_NEW_CONTENT)
            .newType(UPDATED_NEW_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return shopNew;
    }

    @BeforeEach
    public void initTest() {
        shopNew = createEntity(em);
    }

    @Test
    @Transactional
    public void createShopNew() throws Exception {
        int databaseSizeBeforeCreate = shopNewRepository.findAll().size();

        // Create the ShopNew
        restShopNewMockMvc.perform(post("/api/shop-news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopNew)))
            .andExpect(status().isCreated());

        // Validate the ShopNew in the database
        List<ShopNew> shopNewList = shopNewRepository.findAll();
        assertThat(shopNewList).hasSize(databaseSizeBeforeCreate + 1);
        ShopNew testShopNew = shopNewList.get(shopNewList.size() - 1);
        assertThat(testShopNew.getNewTitle()).isEqualTo(DEFAULT_NEW_TITLE);
        assertThat(testShopNew.getNewContent()).isEqualTo(DEFAULT_NEW_CONTENT);
        assertThat(testShopNew.getNewType()).isEqualTo(DEFAULT_NEW_TYPE);
        assertThat(testShopNew.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testShopNew.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testShopNew.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testShopNew.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createShopNewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopNewRepository.findAll().size();

        // Create the ShopNew with an existing ID
        shopNew.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopNewMockMvc.perform(post("/api/shop-news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopNew)))
            .andExpect(status().isBadRequest());

        // Validate the ShopNew in the database
        List<ShopNew> shopNewList = shopNewRepository.findAll();
        assertThat(shopNewList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllShopNews() throws Exception {
        // Initialize the database
        shopNewRepository.saveAndFlush(shopNew);

        // Get all the shopNewList
        restShopNewMockMvc.perform(get("/api/shop-news?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopNew.getId().intValue())))
            .andExpect(jsonPath("$.[*].newTitle").value(hasItem(DEFAULT_NEW_TITLE.toString())))
            .andExpect(jsonPath("$.[*].newContent").value(hasItem(DEFAULT_NEW_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].newType").value(hasItem(DEFAULT_NEW_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.intValue())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.intValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllShopNewsWithEagerRelationshipsIsEnabled() throws Exception {
        ShopNewResource shopNewResource = new ShopNewResource(shopNewRepositoryMock);
        when(shopNewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restShopNewMockMvc = MockMvcBuilders.standaloneSetup(shopNewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restShopNewMockMvc.perform(get("/api/shop-news?eagerload=true"))
        .andExpect(status().isOk());

        verify(shopNewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllShopNewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ShopNewResource shopNewResource = new ShopNewResource(shopNewRepositoryMock);
            when(shopNewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restShopNewMockMvc = MockMvcBuilders.standaloneSetup(shopNewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restShopNewMockMvc.perform(get("/api/shop-news?eagerload=true"))
        .andExpect(status().isOk());

            verify(shopNewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getShopNew() throws Exception {
        // Initialize the database
        shopNewRepository.saveAndFlush(shopNew);

        // Get the shopNew
        restShopNewMockMvc.perform(get("/api/shop-news/{id}", shopNew.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shopNew.getId().intValue()))
            .andExpect(jsonPath("$.newTitle").value(DEFAULT_NEW_TITLE.toString()))
            .andExpect(jsonPath("$.newContent").value(DEFAULT_NEW_CONTENT.toString()))
            .andExpect(jsonPath("$.newType").value(DEFAULT_NEW_TYPE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.intValue()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingShopNew() throws Exception {
        // Get the shopNew
        restShopNewMockMvc.perform(get("/api/shop-news/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShopNew() throws Exception {
        // Initialize the database
        shopNewRepository.saveAndFlush(shopNew);

        int databaseSizeBeforeUpdate = shopNewRepository.findAll().size();

        // Update the shopNew
        ShopNew updatedShopNew = shopNewRepository.findById(shopNew.getId()).get();
        // Disconnect from session so that the updates on updatedShopNew are not directly saved in db
        em.detach(updatedShopNew);
        updatedShopNew
            .newTitle(UPDATED_NEW_TITLE)
            .newContent(UPDATED_NEW_CONTENT)
            .newType(UPDATED_NEW_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restShopNewMockMvc.perform(put("/api/shop-news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShopNew)))
            .andExpect(status().isOk());

        // Validate the ShopNew in the database
        List<ShopNew> shopNewList = shopNewRepository.findAll();
        assertThat(shopNewList).hasSize(databaseSizeBeforeUpdate);
        ShopNew testShopNew = shopNewList.get(shopNewList.size() - 1);
        assertThat(testShopNew.getNewTitle()).isEqualTo(UPDATED_NEW_TITLE);
        assertThat(testShopNew.getNewContent()).isEqualTo(UPDATED_NEW_CONTENT);
        assertThat(testShopNew.getNewType()).isEqualTo(UPDATED_NEW_TYPE);
        assertThat(testShopNew.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testShopNew.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testShopNew.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testShopNew.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingShopNew() throws Exception {
        int databaseSizeBeforeUpdate = shopNewRepository.findAll().size();

        // Create the ShopNew

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopNewMockMvc.perform(put("/api/shop-news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopNew)))
            .andExpect(status().isBadRequest());

        // Validate the ShopNew in the database
        List<ShopNew> shopNewList = shopNewRepository.findAll();
        assertThat(shopNewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShopNew() throws Exception {
        // Initialize the database
        shopNewRepository.saveAndFlush(shopNew);

        int databaseSizeBeforeDelete = shopNewRepository.findAll().size();

        // Delete the shopNew
        restShopNewMockMvc.perform(delete("/api/shop-news/{id}", shopNew.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShopNew> shopNewList = shopNewRepository.findAll();
        assertThat(shopNewList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopNew.class);
        ShopNew shopNew1 = new ShopNew();
        shopNew1.setId(1L);
        ShopNew shopNew2 = new ShopNew();
        shopNew2.setId(shopNew1.getId());
        assertThat(shopNew1).isEqualTo(shopNew2);
        shopNew2.setId(2L);
        assertThat(shopNew1).isNotEqualTo(shopNew2);
        shopNew1.setId(null);
        assertThat(shopNew1).isNotEqualTo(shopNew2);
    }
}
