package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Caracteristique;
import com.mycompany.myapp.repository.CaracteristiqueRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Caracteristique}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CaracteristiqueResource {

    private final Logger log = LoggerFactory.getLogger(CaracteristiqueResource.class);

    private static final String ENTITY_NAME = "caracteristique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaracteristiqueRepository caracteristiqueRepository;

    public CaracteristiqueResource(CaracteristiqueRepository caracteristiqueRepository) {
        this.caracteristiqueRepository = caracteristiqueRepository;
    }

    /**
     * {@code POST  /caracteristiques} : Create a new caracteristique.
     *
     * @param caracteristique the caracteristique to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caracteristique, or with status {@code 400 (Bad Request)} if the caracteristique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/caracteristiques")
    public ResponseEntity<Caracteristique> createCaracteristique(@RequestBody Caracteristique caracteristique) throws URISyntaxException {
        log.debug("REST request to save Caracteristique : {}", caracteristique);
        if (caracteristique.getId() != null) {
            throw new BadRequestAlertException("A new caracteristique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Caracteristique result = caracteristiqueRepository.save(caracteristique);
        return ResponseEntity
            .created(new URI("/api/caracteristiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /caracteristiques/:id} : Updates an existing caracteristique.
     *
     * @param id the id of the caracteristique to save.
     * @param caracteristique the caracteristique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caracteristique,
     * or with status {@code 400 (Bad Request)} if the caracteristique is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caracteristique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/caracteristiques/{id}")
    public ResponseEntity<Caracteristique> updateCaracteristique(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Caracteristique caracteristique
    ) throws URISyntaxException {
        log.debug("REST request to update Caracteristique : {}, {}", id, caracteristique);
        if (caracteristique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caracteristique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caracteristiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Caracteristique result = caracteristiqueRepository.save(caracteristique);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caracteristique.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /caracteristiques/:id} : Partial updates given fields of an existing caracteristique, field will ignore if it is null
     *
     * @param id the id of the caracteristique to save.
     * @param caracteristique the caracteristique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caracteristique,
     * or with status {@code 400 (Bad Request)} if the caracteristique is not valid,
     * or with status {@code 404 (Not Found)} if the caracteristique is not found,
     * or with status {@code 500 (Internal Server Error)} if the caracteristique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/caracteristiques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Caracteristique> partialUpdateCaracteristique(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Caracteristique caracteristique
    ) throws URISyntaxException {
        log.debug("REST request to partial update Caracteristique partially : {}, {}", id, caracteristique);
        if (caracteristique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caracteristique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caracteristiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Caracteristique> result = caracteristiqueRepository
            .findById(caracteristique.getId())
            .map(existingCaracteristique -> {
                if (caracteristique.getiDCaracteristique() != null) {
                    existingCaracteristique.setiDCaracteristique(caracteristique.getiDCaracteristique());
                }
                if (caracteristique.getMarque() != null) {
                    existingCaracteristique.setMarque(caracteristique.getMarque());
                }
                if (caracteristique.getModele() != null) {
                    existingCaracteristique.setModele(caracteristique.getModele());
                }
                if (caracteristique.getTaille() != null) {
                    existingCaracteristique.setTaille(caracteristique.getTaille());
                }
                if (caracteristique.getCouleur() != null) {
                    existingCaracteristique.setCouleur(caracteristique.getCouleur());
                }

                return existingCaracteristique;
            })
            .map(caracteristiqueRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caracteristique.getId().toString())
        );
    }

    /**
     * {@code GET  /caracteristiques} : get all the caracteristiques.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caracteristiques in body.
     */
    @GetMapping("/caracteristiques")
    public List<Caracteristique> getAllCaracteristiques(@RequestParam(required = false) String filter) {
        if ("produit-is-null".equals(filter)) {
            log.debug("REST request to get all Caracteristiques where produit is null");
            return StreamSupport
                .stream(caracteristiqueRepository.findAll().spliterator(), false)
                .filter(caracteristique -> caracteristique.getProduit() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Caracteristiques");
        return caracteristiqueRepository.findAll();
    }

    /**
     * {@code GET  /caracteristiques/:id} : get the "id" caracteristique.
     *
     * @param id the id of the caracteristique to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caracteristique, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/caracteristiques/{id}")
    public ResponseEntity<Caracteristique> getCaracteristique(@PathVariable Long id) {
        log.debug("REST request to get Caracteristique : {}", id);
        Optional<Caracteristique> caracteristique = caracteristiqueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(caracteristique);
    }

    /**
     * {@code DELETE  /caracteristiques/:id} : delete the "id" caracteristique.
     *
     * @param id the id of the caracteristique to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/caracteristiques/{id}")
    public ResponseEntity<Void> deleteCaracteristique(@PathVariable Long id) {
        log.debug("REST request to delete Caracteristique : {}", id);
        caracteristiqueRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
