package com.wlminus.web.rest;

import com.wlminus.JamilaApp;
import com.wlminus.domain.Ward;
import com.wlminus.repository.WardRepository;
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
 * Integration tests for the {@link WardResource} REST controller.
 */
@SpringBootTest(classes = JamilaApp.class)
public class WardResourceIT {

    private static final String DEFAULT_WARD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_WARD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WARD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_WARD_TYPE = "BBBBBBBBBB";

    @Autowired
    private WardRepository wardRepository;

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

    private MockMvc restWardMockMvc;

    private Ward ward;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WardResource wardResource = new WardResource(wardRepository);
        this.restWardMockMvc = MockMvcBuilders.standaloneSetup(wardResource)
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
    public static Ward createEntity(EntityManager em) {
        Ward ward = new Ward()
            .wardName(DEFAULT_WARD_NAME)
            .wardType(DEFAULT_WARD_TYPE);
        return ward;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ward createUpdatedEntity(EntityManager em) {
        Ward ward = new Ward()
            .wardName(UPDATED_WARD_NAME)
            .wardType(UPDATED_WARD_TYPE);
        return ward;
    }

    @BeforeEach
    public void initTest() {
        ward = createEntity(em);
    }

    @Test
    @Transactional
    public void createWard() throws Exception {
        int databaseSizeBeforeCreate = wardRepository.findAll().size();

        // Create the Ward
        restWardMockMvc.perform(post("/api/wards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ward)))
            .andExpect(status().isCreated());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeCreate + 1);
        Ward testWard = wardList.get(wardList.size() - 1);
        assertThat(testWard.getWardName()).isEqualTo(DEFAULT_WARD_NAME);
        assertThat(testWard.getWardType()).isEqualTo(DEFAULT_WARD_TYPE);
    }

    @Test
    @Transactional
    public void createWardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wardRepository.findAll().size();

        // Create the Ward with an existing ID
        ward.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWardMockMvc.perform(post("/api/wards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ward)))
            .andExpect(status().isBadRequest());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllWards() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList
        restWardMockMvc.perform(get("/api/wards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ward.getId().intValue())))
            .andExpect(jsonPath("$.[*].wardName").value(hasItem(DEFAULT_WARD_NAME.toString())))
            .andExpect(jsonPath("$.[*].wardType").value(hasItem(DEFAULT_WARD_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getWard() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get the ward
        restWardMockMvc.perform(get("/api/wards/{id}", ward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ward.getId().intValue()))
            .andExpect(jsonPath("$.wardName").value(DEFAULT_WARD_NAME.toString()))
            .andExpect(jsonPath("$.wardType").value(DEFAULT_WARD_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWard() throws Exception {
        // Get the ward
        restWardMockMvc.perform(get("/api/wards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWard() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        int databaseSizeBeforeUpdate = wardRepository.findAll().size();

        // Update the ward
        Ward updatedWard = wardRepository.findById(ward.getId()).get();
        // Disconnect from session so that the updates on updatedWard are not directly saved in db
        em.detach(updatedWard);
        updatedWard
            .wardName(UPDATED_WARD_NAME)
            .wardType(UPDATED_WARD_TYPE);

        restWardMockMvc.perform(put("/api/wards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWard)))
            .andExpect(status().isOk());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
        Ward testWard = wardList.get(wardList.size() - 1);
        assertThat(testWard.getWardName()).isEqualTo(UPDATED_WARD_NAME);
        assertThat(testWard.getWardType()).isEqualTo(UPDATED_WARD_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingWard() throws Exception {
        int databaseSizeBeforeUpdate = wardRepository.findAll().size();

        // Create the Ward

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWardMockMvc.perform(put("/api/wards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ward)))
            .andExpect(status().isBadRequest());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWard() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        int databaseSizeBeforeDelete = wardRepository.findAll().size();

        // Delete the ward
        restWardMockMvc.perform(delete("/api/wards/{id}", ward.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ward.class);
        Ward ward1 = new Ward();
        ward1.setId(1L);
        Ward ward2 = new Ward();
        ward2.setId(ward1.getId());
        assertThat(ward1).isEqualTo(ward2);
        ward2.setId(2L);
        assertThat(ward1).isNotEqualTo(ward2);
        ward1.setId(null);
        assertThat(ward1).isNotEqualTo(ward2);
    }
}
