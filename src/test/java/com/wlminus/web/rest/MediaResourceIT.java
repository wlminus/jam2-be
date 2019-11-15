package com.wlminus.web.rest;

import com.wlminus.JamilaApp;
import com.wlminus.domain.Media;
import com.wlminus.repository.MediaRepository;
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
 * Integration tests for the {@link MediaResource} REST controller.
 */
@SpringBootTest(classes = JamilaApp.class)
public class MediaResourceIT {

    private static final String DEFAULT_MEDIA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIA_URL = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_URL = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIA_ALT = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_ALT = "BBBBBBBBBB";

    private static final String DEFAULT_UPLOAD_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_UPLOAD_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_UPLOAD_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_UPLOAD_MONTH = "BBBBBBBBBB";

    @Autowired
    private MediaRepository mediaRepository;

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

    private MockMvc restMediaMockMvc;

    private Media media;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MediaResource mediaResource = new MediaResource(mediaRepository);
        this.restMediaMockMvc = MockMvcBuilders.standaloneSetup(mediaResource)
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
    public static Media createEntity(EntityManager em) {
        Media media = new Media()
            .mediaName(DEFAULT_MEDIA_NAME)
            .mediaType(DEFAULT_MEDIA_TYPE)
            .mediaURL(DEFAULT_MEDIA_URL)
            .mediaAlt(DEFAULT_MEDIA_ALT)
            .uploadYear(DEFAULT_UPLOAD_YEAR)
            .uploadMonth(DEFAULT_UPLOAD_MONTH);
        return media;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Media createUpdatedEntity(EntityManager em) {
        Media media = new Media()
            .mediaName(UPDATED_MEDIA_NAME)
            .mediaType(UPDATED_MEDIA_TYPE)
            .mediaURL(UPDATED_MEDIA_URL)
            .mediaAlt(UPDATED_MEDIA_ALT)
            .uploadYear(UPDATED_UPLOAD_YEAR)
            .uploadMonth(UPDATED_UPLOAD_MONTH);
        return media;
    }

    @BeforeEach
    public void initTest() {
        media = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedia() throws Exception {
        int databaseSizeBeforeCreate = mediaRepository.findAll().size();

        // Create the Media
        restMediaMockMvc.perform(post("/api/media")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(media)))
            .andExpect(status().isCreated());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeCreate + 1);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getMediaName()).isEqualTo(DEFAULT_MEDIA_NAME);
        assertThat(testMedia.getMediaType()).isEqualTo(DEFAULT_MEDIA_TYPE);
        assertThat(testMedia.getMediaURL()).isEqualTo(DEFAULT_MEDIA_URL);
        assertThat(testMedia.getMediaAlt()).isEqualTo(DEFAULT_MEDIA_ALT);
        assertThat(testMedia.getUploadYear()).isEqualTo(DEFAULT_UPLOAD_YEAR);
        assertThat(testMedia.getUploadMonth()).isEqualTo(DEFAULT_UPLOAD_MONTH);
    }

    @Test
    @Transactional
    public void createMediaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mediaRepository.findAll().size();

        // Create the Media with an existing ID
        media.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMediaMockMvc.perform(post("/api/media")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(media)))
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList
        restMediaMockMvc.perform(get("/api/media?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(media.getId().intValue())))
            .andExpect(jsonPath("$.[*].mediaName").value(hasItem(DEFAULT_MEDIA_NAME)))
            .andExpect(jsonPath("$.[*].mediaType").value(hasItem(DEFAULT_MEDIA_TYPE)))
            .andExpect(jsonPath("$.[*].mediaURL").value(hasItem(DEFAULT_MEDIA_URL)))
            .andExpect(jsonPath("$.[*].mediaAlt").value(hasItem(DEFAULT_MEDIA_ALT)))
            .andExpect(jsonPath("$.[*].uploadYear").value(hasItem(DEFAULT_UPLOAD_YEAR)))
            .andExpect(jsonPath("$.[*].uploadMonth").value(hasItem(DEFAULT_UPLOAD_MONTH)));
    }
    
    @Test
    @Transactional
    public void getMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get the media
        restMediaMockMvc.perform(get("/api/media/{id}", media.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(media.getId().intValue()))
            .andExpect(jsonPath("$.mediaName").value(DEFAULT_MEDIA_NAME))
            .andExpect(jsonPath("$.mediaType").value(DEFAULT_MEDIA_TYPE))
            .andExpect(jsonPath("$.mediaURL").value(DEFAULT_MEDIA_URL))
            .andExpect(jsonPath("$.mediaAlt").value(DEFAULT_MEDIA_ALT))
            .andExpect(jsonPath("$.uploadYear").value(DEFAULT_UPLOAD_YEAR))
            .andExpect(jsonPath("$.uploadMonth").value(DEFAULT_UPLOAD_MONTH));
    }

    @Test
    @Transactional
    public void getNonExistingMedia() throws Exception {
        // Get the media
        restMediaMockMvc.perform(get("/api/media/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Update the media
        Media updatedMedia = mediaRepository.findById(media.getId()).get();
        // Disconnect from session so that the updates on updatedMedia are not directly saved in db
        em.detach(updatedMedia);
        updatedMedia
            .mediaName(UPDATED_MEDIA_NAME)
            .mediaType(UPDATED_MEDIA_TYPE)
            .mediaURL(UPDATED_MEDIA_URL)
            .mediaAlt(UPDATED_MEDIA_ALT)
            .uploadYear(UPDATED_UPLOAD_YEAR)
            .uploadMonth(UPDATED_UPLOAD_MONTH);

        restMediaMockMvc.perform(put("/api/media")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedia)))
            .andExpect(status().isOk());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getMediaName()).isEqualTo(UPDATED_MEDIA_NAME);
        assertThat(testMedia.getMediaType()).isEqualTo(UPDATED_MEDIA_TYPE);
        assertThat(testMedia.getMediaURL()).isEqualTo(UPDATED_MEDIA_URL);
        assertThat(testMedia.getMediaAlt()).isEqualTo(UPDATED_MEDIA_ALT);
        assertThat(testMedia.getUploadYear()).isEqualTo(UPDATED_UPLOAD_YEAR);
        assertThat(testMedia.getUploadMonth()).isEqualTo(UPDATED_UPLOAD_MONTH);
    }

    @Test
    @Transactional
    public void updateNonExistingMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Create the Media

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaMockMvc.perform(put("/api/media")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(media)))
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeDelete = mediaRepository.findAll().size();

        // Delete the media
        restMediaMockMvc.perform(delete("/api/media/{id}", media.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
