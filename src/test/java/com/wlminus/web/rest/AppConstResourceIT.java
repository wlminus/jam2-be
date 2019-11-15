package com.wlminus.web.rest;

import com.wlminus.JamilaApp;
import com.wlminus.domain.AppConst;
import com.wlminus.repository.AppConstRepository;
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

import com.wlminus.domain.enumeration.ConfigKey;
/**
 * Integration tests for the {@link AppConstResource} REST controller.
 */
@SpringBootTest(classes = JamilaApp.class)
public class AppConstResourceIT {

    private static final ConfigKey DEFAULT_CONST_KEY = ConfigKey.HOME_PRODUCT_LIST;
    private static final ConfigKey UPDATED_CONST_KEY = ConfigKey.REALESE_STATUS;

    private static final String DEFAULT_CONST_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONST_VALUE = "BBBBBBBBBB";

    @Autowired
    private AppConstRepository appConstRepository;

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

    private MockMvc restAppConstMockMvc;

    private AppConst appConst;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppConstResource appConstResource = new AppConstResource(appConstRepository);
        this.restAppConstMockMvc = MockMvcBuilders.standaloneSetup(appConstResource)
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
    public static AppConst createEntity(EntityManager em) {
        AppConst appConst = new AppConst()
            .constKey(DEFAULT_CONST_KEY)
            .constValue(DEFAULT_CONST_VALUE);
        return appConst;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppConst createUpdatedEntity(EntityManager em) {
        AppConst appConst = new AppConst()
            .constKey(UPDATED_CONST_KEY)
            .constValue(UPDATED_CONST_VALUE);
        return appConst;
    }

    @BeforeEach
    public void initTest() {
        appConst = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppConst() throws Exception {
        int databaseSizeBeforeCreate = appConstRepository.findAll().size();

        // Create the AppConst
        restAppConstMockMvc.perform(post("/api/app-consts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConst)))
            .andExpect(status().isCreated());

        // Validate the AppConst in the database
        List<AppConst> appConstList = appConstRepository.findAll();
        assertThat(appConstList).hasSize(databaseSizeBeforeCreate + 1);
        AppConst testAppConst = appConstList.get(appConstList.size() - 1);
        assertThat(testAppConst.getConstKey()).isEqualTo(DEFAULT_CONST_KEY);
        assertThat(testAppConst.getConstValue()).isEqualTo(DEFAULT_CONST_VALUE);
    }

    @Test
    @Transactional
    public void createAppConstWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appConstRepository.findAll().size();

        // Create the AppConst with an existing ID
        appConst.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppConstMockMvc.perform(post("/api/app-consts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConst)))
            .andExpect(status().isBadRequest());

        // Validate the AppConst in the database
        List<AppConst> appConstList = appConstRepository.findAll();
        assertThat(appConstList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAppConsts() throws Exception {
        // Initialize the database
        appConstRepository.saveAndFlush(appConst);

        // Get all the appConstList
        restAppConstMockMvc.perform(get("/api/app-consts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appConst.getId().intValue())))
            .andExpect(jsonPath("$.[*].constKey").value(hasItem(DEFAULT_CONST_KEY.toString())))
            .andExpect(jsonPath("$.[*].constValue").value(hasItem(DEFAULT_CONST_VALUE)));
    }
    
    @Test
    @Transactional
    public void getAppConst() throws Exception {
        // Initialize the database
        appConstRepository.saveAndFlush(appConst);

        // Get the appConst
        restAppConstMockMvc.perform(get("/api/app-consts/{id}", appConst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appConst.getId().intValue()))
            .andExpect(jsonPath("$.constKey").value(DEFAULT_CONST_KEY.toString()))
            .andExpect(jsonPath("$.constValue").value(DEFAULT_CONST_VALUE));
    }

    @Test
    @Transactional
    public void getNonExistingAppConst() throws Exception {
        // Get the appConst
        restAppConstMockMvc.perform(get("/api/app-consts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppConst() throws Exception {
        // Initialize the database
        appConstRepository.saveAndFlush(appConst);

        int databaseSizeBeforeUpdate = appConstRepository.findAll().size();

        // Update the appConst
        AppConst updatedAppConst = appConstRepository.findById(appConst.getId()).get();
        // Disconnect from session so that the updates on updatedAppConst are not directly saved in db
        em.detach(updatedAppConst);
        updatedAppConst
            .constKey(UPDATED_CONST_KEY)
            .constValue(UPDATED_CONST_VALUE);

        restAppConstMockMvc.perform(put("/api/app-consts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppConst)))
            .andExpect(status().isOk());

        // Validate the AppConst in the database
        List<AppConst> appConstList = appConstRepository.findAll();
        assertThat(appConstList).hasSize(databaseSizeBeforeUpdate);
        AppConst testAppConst = appConstList.get(appConstList.size() - 1);
        assertThat(testAppConst.getConstKey()).isEqualTo(UPDATED_CONST_KEY);
        assertThat(testAppConst.getConstValue()).isEqualTo(UPDATED_CONST_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingAppConst() throws Exception {
        int databaseSizeBeforeUpdate = appConstRepository.findAll().size();

        // Create the AppConst

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppConstMockMvc.perform(put("/api/app-consts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConst)))
            .andExpect(status().isBadRequest());

        // Validate the AppConst in the database
        List<AppConst> appConstList = appConstRepository.findAll();
        assertThat(appConstList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppConst() throws Exception {
        // Initialize the database
        appConstRepository.saveAndFlush(appConst);

        int databaseSizeBeforeDelete = appConstRepository.findAll().size();

        // Delete the appConst
        restAppConstMockMvc.perform(delete("/api/app-consts/{id}", appConst.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppConst> appConstList = appConstRepository.findAll();
        assertThat(appConstList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
