package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Connexion;
import com.mycompany.myapp.repository.ConnexionRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Connexion}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConnexionResource {

    private final Logger log = LoggerFactory.getLogger(ConnexionResource.class);

    private static final String ENTITY_NAME = "connexion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConnexionRepository connexionRepository;

    public ConnexionResource(ConnexionRepository connexionRepository) {
        this.connexionRepository = connexionRepository;
    }

    /**
     * {@code POST  /connexions} : Create a new connexion.
     *
     * @param connexion the connexion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new connexion, or with status {@code 400 (Bad Request)} if the connexion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/connexions")
    public ResponseEntity<Connexion> createConnexion(@RequestBody Connexion connexion) throws URISyntaxException {
        log.debug("REST request to save Connexion : {}", connexion);
        if (connexion.getId() != null) {
            throw new BadRequestAlertException("A new connexion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Connexion result = connexionRepository.save(connexion);
        return ResponseEntity
            .created(new URI("/api/connexions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /connexions/:id} : Updates an existing connexion.
     *
     * @param id the id of the connexion to save.
     * @param connexion the connexion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated connexion,
     * or with status {@code 400 (Bad Request)} if the connexion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the connexion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/connexions/{id}")
    public ResponseEntity<Connexion> updateConnexion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Connexion connexion
    ) throws URISyntaxException {
        log.debug("REST request to update Connexion : {}, {}", id, connexion);
        if (connexion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, connexion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!connexionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Connexion result = connexionRepository.save(connexion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, connexion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /connexions/:id} : Partial updates given fields of an existing connexion, field will ignore if it is null
     *
     * @param id the id of the connexion to save.
     * @param connexion the connexion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated connexion,
     * or with status {@code 400 (Bad Request)} if the connexion is not valid,
     * or with status {@code 404 (Not Found)} if the connexion is not found,
     * or with status {@code 500 (Internal Server Error)} if the connexion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/connexions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Connexion> partialUpdateConnexion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Connexion connexion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Connexion partially : {}, {}", id, connexion);
        if (connexion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, connexion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!connexionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Connexion> result = connexionRepository
            .findById(connexion.getId())
            .map(existingConnexion -> {
                if (connexion.getiDConnexion() != null) {
                    existingConnexion.setiDConnexion(connexion.getiDConnexion());
                }
                if (connexion.getUsername() != null) {
                    existingConnexion.setUsername(connexion.getUsername());
                }
                if (connexion.getPassword() != null) {
                    existingConnexion.setPassword(connexion.getPassword());
                }

                return existingConnexion;
            })
            .map(connexionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, connexion.getId().toString())
        );
    }

    /**
     * {@code GET  /connexions} : get all the connexions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of connexions in body.
     */
    @GetMapping("/connexions")
    public List<Connexion> getAllConnexions() {
        log.debug("REST request to get all Connexions");
        return connexionRepository.findAll();
    }

    /**
     * {@code GET  /connexions/:id} : get the "id" connexion.
     *
     * @param id the id of the connexion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the connexion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/connexions/{id}")
    public ResponseEntity<Connexion> getConnexion(@PathVariable Long id) {
        log.debug("REST request to get Connexion : {}", id);
        Optional<Connexion> connexion = connexionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(connexion);
    }

    /**
     * {@code DELETE  /connexions/:id} : delete the "id" connexion.
     *
     * @param id the id of the connexion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/connexions/{id}")
    public ResponseEntity<Void> deleteConnexion(@PathVariable Long id) {
        log.debug("REST request to delete Connexion : {}", id);
        connexionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
