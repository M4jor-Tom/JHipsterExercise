package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Famille;
import com.mycompany.myapp.repository.FamilleRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Famille}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FamilleResource {

    private final Logger log = LoggerFactory.getLogger(FamilleResource.class);

    private static final String ENTITY_NAME = "famille";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilleRepository familleRepository;

    public FamilleResource(FamilleRepository familleRepository) {
        this.familleRepository = familleRepository;
    }

    /**
     * {@code POST  /familles} : Create a new famille.
     *
     * @param famille the famille to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new famille, or with status {@code 400 (Bad Request)} if the famille has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/familles")
    public ResponseEntity<Famille> createFamille(@RequestBody Famille famille) throws URISyntaxException {
        log.debug("REST request to save Famille : {}", famille);
        if (famille.getId() != null) {
            throw new BadRequestAlertException("A new famille cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Famille result = familleRepository.save(famille);
        return ResponseEntity
            .created(new URI("/api/familles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /familles/:id} : Updates an existing famille.
     *
     * @param id the id of the famille to save.
     * @param famille the famille to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated famille,
     * or with status {@code 400 (Bad Request)} if the famille is not valid,
     * or with status {@code 500 (Internal Server Error)} if the famille couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/familles/{id}")
    public ResponseEntity<Famille> updateFamille(@PathVariable(value = "id", required = false) final Long id, @RequestBody Famille famille)
        throws URISyntaxException {
        log.debug("REST request to update Famille : {}, {}", id, famille);
        if (famille.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, famille.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Famille result = familleRepository.save(famille);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, famille.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /familles/:id} : Partial updates given fields of an existing famille, field will ignore if it is null
     *
     * @param id the id of the famille to save.
     * @param famille the famille to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated famille,
     * or with status {@code 400 (Bad Request)} if the famille is not valid,
     * or with status {@code 404 (Not Found)} if the famille is not found,
     * or with status {@code 500 (Internal Server Error)} if the famille couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/familles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Famille> partialUpdateFamille(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Famille famille
    ) throws URISyntaxException {
        log.debug("REST request to partial update Famille partially : {}, {}", id, famille);
        if (famille.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, famille.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Famille> result = familleRepository
            .findById(famille.getId())
            .map(existingFamille -> {
                if (famille.getiDFamille() != null) {
                    existingFamille.setiDFamille(famille.getiDFamille());
                }
                if (famille.getFamille() != null) {
                    existingFamille.setFamille(famille.getFamille());
                }

                return existingFamille;
            })
            .map(familleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, famille.getId().toString())
        );
    }

    /**
     * {@code GET  /familles} : get all the familles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familles in body.
     */
    @GetMapping("/familles")
    public List<Famille> getAllFamilles() {
        log.debug("REST request to get all Familles");
        return familleRepository.findAll();
    }

    /**
     * {@code GET  /familles/:id} : get the "id" famille.
     *
     * @param id the id of the famille to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the famille, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/familles/{id}")
    public ResponseEntity<Famille> getFamille(@PathVariable Long id) {
        log.debug("REST request to get Famille : {}", id);
        Optional<Famille> famille = familleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(famille);
    }

    /**
     * {@code DELETE  /familles/:id} : delete the "id" famille.
     *
     * @param id the id of the famille to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/familles/{id}")
    public ResponseEntity<Void> deleteFamille(@PathVariable Long id) {
        log.debug("REST request to delete Famille : {}", id);
        familleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
