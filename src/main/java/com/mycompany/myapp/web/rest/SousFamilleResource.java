package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SousFamille;
import com.mycompany.myapp.repository.SousFamilleRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SousFamille}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SousFamilleResource {

    private final Logger log = LoggerFactory.getLogger(SousFamilleResource.class);

    private static final String ENTITY_NAME = "sousFamille";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousFamilleRepository sousFamilleRepository;

    public SousFamilleResource(SousFamilleRepository sousFamilleRepository) {
        this.sousFamilleRepository = sousFamilleRepository;
    }

    /**
     * {@code POST  /sous-familles} : Create a new sousFamille.
     *
     * @param sousFamille the sousFamille to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousFamille, or with status {@code 400 (Bad Request)} if the sousFamille has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-familles")
    public ResponseEntity<SousFamille> createSousFamille(@RequestBody SousFamille sousFamille) throws URISyntaxException {
        log.debug("REST request to save SousFamille : {}", sousFamille);
        if (sousFamille.getId() != null) {
            throw new BadRequestAlertException("A new sousFamille cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousFamille result = sousFamilleRepository.save(sousFamille);
        return ResponseEntity
            .created(new URI("/api/sous-familles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-familles/:id} : Updates an existing sousFamille.
     *
     * @param id the id of the sousFamille to save.
     * @param sousFamille the sousFamille to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousFamille,
     * or with status {@code 400 (Bad Request)} if the sousFamille is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousFamille couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-familles/{id}")
    public ResponseEntity<SousFamille> updateSousFamille(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SousFamille sousFamille
    ) throws URISyntaxException {
        log.debug("REST request to update SousFamille : {}, {}", id, sousFamille);
        if (sousFamille.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousFamille.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousFamilleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousFamille result = sousFamilleRepository.save(sousFamille);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sousFamille.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-familles/:id} : Partial updates given fields of an existing sousFamille, field will ignore if it is null
     *
     * @param id the id of the sousFamille to save.
     * @param sousFamille the sousFamille to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousFamille,
     * or with status {@code 400 (Bad Request)} if the sousFamille is not valid,
     * or with status {@code 404 (Not Found)} if the sousFamille is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousFamille couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-familles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousFamille> partialUpdateSousFamille(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SousFamille sousFamille
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousFamille partially : {}, {}", id, sousFamille);
        if (sousFamille.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousFamille.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousFamilleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousFamille> result = sousFamilleRepository
            .findById(sousFamille.getId())
            .map(existingSousFamille -> {
                if (sousFamille.getiDSousFamille() != null) {
                    existingSousFamille.setiDSousFamille(sousFamille.getiDSousFamille());
                }
                if (sousFamille.getSousFamille() != null) {
                    existingSousFamille.setSousFamille(sousFamille.getSousFamille());
                }

                return existingSousFamille;
            })
            .map(sousFamilleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sousFamille.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-familles} : get all the sousFamilles.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousFamilles in body.
     */
    @GetMapping("/sous-familles")
    public List<SousFamille> getAllSousFamilles(@RequestParam(required = false) String filter) {
        if ("famille-is-null".equals(filter)) {
            log.debug("REST request to get all SousFamilles where famille is null");
            return StreamSupport
                .stream(sousFamilleRepository.findAll().spliterator(), false)
                .filter(sousFamille -> sousFamille.getFamille() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all SousFamilles");
        return sousFamilleRepository.findAll();
    }

    /**
     * {@code GET  /sous-familles/:id} : get the "id" sousFamille.
     *
     * @param id the id of the sousFamille to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousFamille, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-familles/{id}")
    public ResponseEntity<SousFamille> getSousFamille(@PathVariable Long id) {
        log.debug("REST request to get SousFamille : {}", id);
        Optional<SousFamille> sousFamille = sousFamilleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sousFamille);
    }

    /**
     * {@code DELETE  /sous-familles/:id} : delete the "id" sousFamille.
     *
     * @param id the id of the sousFamille to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-familles/{id}")
    public ResponseEntity<Void> deleteSousFamille(@PathVariable Long id) {
        log.debug("REST request to delete SousFamille : {}", id);
        sousFamilleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
