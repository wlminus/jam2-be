package com.wlminus.web.rest;

import com.wlminus.config.Constants;
import com.wlminus.domain.Media;
import com.wlminus.repository.MediaRepository;
import com.wlminus.service.MediaUploadService;
import com.wlminus.service.errors.FileStorageException;
import com.wlminus.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.wlminus.domain.Media}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MediaResource {

    private final Logger log = LoggerFactory.getLogger(MediaResource.class);

    private static final String ENTITY_NAME = "media";
    private final MediaUploadService mediaUploadService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MediaRepository mediaRepository;

    public MediaResource(MediaRepository mediaRepository, MediaUploadService mediaUploadService) {
        this.mediaRepository = mediaRepository;
        this.mediaUploadService = mediaUploadService;
    }

    /**
     * Upload a file and register into database Media
     * @param file a file
     * @return Entity media in database
     */
    @PostMapping("/media")
    public ResponseEntity<Media> uploadMedia(@RequestParam("filepond") MultipartFile file) {
        log.debug("REST: request to upload a Media and save to database");
        if (!Arrays.stream(Constants.ALLOWED_FILE_TYPE).anyMatch(file.getContentType()::equals)) {
            throw new BadRequestAlertException("File type are not allowed", ENTITY_NAME, "file-type-not-allowed");
        }
        String fileName = file.getOriginalFilename();
        if (fileName.equals(".") || fileName.equals("..")) {
            throw new BadRequestAlertException("File name are not allowed", ENTITY_NAME, "file-name-not-allowed");
        }
        if (fileName.length() > Constants.MAX_FILE_NAME_SIZE) {
            throw new BadRequestAlertException("File name out of range", ENTITY_NAME, "file-name-too-long");
        }
        int dot = fileName.indexOf('.');
        String basename = dot == -1 ? fileName : fileName.substring(0, dot);
        if(basename.contains("..") || basename.contains("/") || basename.contains("\0")) {
            throw new BadRequestAlertException("File name are not allowed", ENTITY_NAME, "file-name-not-allowed");
        }

        try {
            log.debug("REST: Start write file to disk");
            String[] savedMeta = mediaUploadService.storeFile(file);

            Media fileInDatabase = new Media();

            fileInDatabase.setMediaName(savedMeta[0]);
            fileInDatabase.setMediaType(file.getContentType());
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/media/files/")
                .path(savedMeta[0])
                .toUriString();
            fileInDatabase.setMediaURL(fileDownloadUri);
            fileInDatabase.setUploadYear(savedMeta[1]);
            fileInDatabase.setUploadMonth(savedMeta[2]);

            log.debug("REST: Start save entity to database");
            Media result = mediaRepository.save(fileInDatabase);
            return ResponseEntity.created(new URI("/api/media/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);

        } catch (FileStorageException ex) {
            throw new BadRequestAlertException(ex.getMessage(), ENTITY_NAME, "file-storage-ex");
        } catch (Exception ex2) {
            throw new BadRequestAlertException(ex2.getMessage(), ENTITY_NAME, "run-time-ex");
        }
    }

    /**
     * Serving a file from request by name
     * @param fileName the name of file requested
     * @return a response with file resource
     */
    @GetMapping(value = "/media/files/{fileName}")
    public ResponseEntity<Resource> servingFileByName(@PathVariable String fileName) {
        log.debug("REST: Start serving file by name");
        Optional<Media> dataMedia = mediaRepository.findByMediaName(fileName);
        if (dataMedia.isPresent()) {
            Media data = dataMedia.get();
            Resource res = mediaUploadService.loadFileAsResource(data.getMediaName(), data.getUploadYear(), data.getUploadMonth());
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(data.getMediaType()))
                .body(res);
        } else {
            throw new BadRequestAlertException("File not found", ENTITY_NAME, "file-not-found");
        }
    }


    /**
     * {@code PUT  /media} : Updates an existing media.
     *
     * @param media the media to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated media,
     * or with status {@code 400 (Bad Request)} if the media is not valid,
     * or with status {@code 500 (Internal Server Error)} if the media couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/media")
    public ResponseEntity<Media> updateMedia(@Valid @RequestBody Media media) throws URISyntaxException {
        log.debug("REST request to update Media : {}", media);
        if (media.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Media result = mediaRepository.save(media);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, media.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /media} : get all the media.
     *
     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of media in body.
     */
    @GetMapping("/media")
    public ResponseEntity<List<Media>> getAllMedia(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Media");
        Page<Media> page = mediaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /media/:id} : get the "id" media.
     *
     * @param id the id of the media to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the media, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/media/{id}")
    public ResponseEntity<Media> getMedia(@PathVariable Long id) {
        log.debug("REST request to get Media : {}", id);
        Optional<Media> media = mediaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(media);
    }

    /**
     * DELETE  /media/:id : delete the "id" media.
     *
     * @param id the id of the media to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/media/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        log.debug("REST request to delete file and it's relation Media entity  : {}", id);
        Optional<Media> media = mediaRepository.findById(id);
        if (media.isPresent()) {
            Media data = media.get();
            mediaUploadService.deleteFile(data.getMediaName(), data.getUploadYear(), data.getUploadMonth());
            mediaRepository.delete(data);
            return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
        }
        else {
            throw new BadRequestAlertException("Delete file fail", ENTITY_NAME, "delete-fail");
        }
    }
}
