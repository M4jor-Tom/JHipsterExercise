package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Family;
import com.mycompany.myapp.repository.FamilyRepository;
import com.mycompany.myapp.service.FamilyQueryService;
import com.mycompany.myapp.service.FamilyService;
import com.mycompany.myapp.service.criteria.FamilyCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Family}.
 */
@RestController
@RequestMapping("/api")
public class FamilyResource {

    private final Logger log = LoggerFactory.getLogger(FamilyResource.class);

    private static final String ENTITY_NAME = "family";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilyService familyService;

    private final FamilyRepository familyRepository;

    private final FamilyQueryService familyQueryService;

    public FamilyResource(FamilyService familyService, FamilyRepository familyRepository, FamilyQueryService familyQueryService) {
        this.familyService = familyService;
        this.familyRepository = familyRepository;
        this.familyQueryService = familyQueryService;
    }

    /**
     * {@code POST  /families} : Create a new family.
     *
     * @param family the family to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new family, or with status {@code 400 (Bad Request)} if the family has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/families")
    public ResponseEntity<Family> createFamily(@Valid @RequestBody Family family) throws URISyntaxException {
        log.debug("REST request to save Family : {}", family);
        if (family.getId() != null) {
            throw new BadRequestAlertException("A new family cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Family result = familyService.save(family);
        return ResponseEntity
            .created(new URI("/api/families/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /families/:id} : Updates an existing family.
     *
     * @param id the id of the family to save.
     * @param family the family to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated family,
     * or with status {@code 400 (Bad Request)} if the family is not valid,
     * or with status {@code 500 (Internal Server Error)} if the family couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/families/{id}")
    public ResponseEntity<Family> updateFamily(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Family family
    ) throws URISyntaxException {
        log.debug("REST request to update Family : {}, {}", id, family);
        if (family.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, family.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Family result = familyService.save(family);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, family.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /families/:id} : Partial updates given fields of an existing family, field will ignore if it is null
     *
     * @param id the id of the family to save.
     * @param family the family to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated family,
     * or with status {@code 400 (Bad Request)} if the family is not valid,
     * or with status {@code 404 (Not Found)} if the family is not found,
     * or with status {@code 500 (Internal Server Error)} if the family couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/families/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Family> partialUpdateFamily(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Family family
    ) throws URISyntaxException {
        log.debug("REST request to partial update Family partially : {}, {}", id, family);
        if (family.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, family.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Family> result = familyService.partialUpdate(family);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, family.getId().toString())
        );
    }

    /**
     * {@code GET  /families} : get all the families.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of families in body.
     */
    @GetMapping("/families")
    public ResponseEntity<List<Family>> getAllFamilies(FamilyCriteria criteria) {
        log.debug("REST request to get Families by criteria: {}", criteria);
        List<Family> entityList = familyQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /families/count} : count all the families.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/families/count")
    public ResponseEntity<Long> countFamilies(FamilyCriteria criteria) {
        log.debug("REST request to count Families by criteria: {}", criteria);
        return ResponseEntity.ok().body(familyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /families/:id} : get the "id" family.
     *
     * @param id the id of the family to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the family, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/families/{id}")
    public ResponseEntity<Family> getFamily(@PathVariable Long id) {
        log.debug("REST request to get Family : {}", id);
        Optional<Family> family = familyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(family);
    }

    /**
     * {@code DELETE  /families/:id} : delete the "id" family.
     *
     * @param id the id of the family to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/families/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable Long id) {
        log.debug("REST request to delete Family : {}", id);
        familyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
