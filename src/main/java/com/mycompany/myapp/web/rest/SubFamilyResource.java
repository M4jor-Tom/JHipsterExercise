package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SubFamily;
import com.mycompany.myapp.repository.SubFamilyRepository;
import com.mycompany.myapp.service.SubFamilyQueryService;
import com.mycompany.myapp.service.SubFamilyService;
import com.mycompany.myapp.service.criteria.SubFamilyCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SubFamily}.
 */
@RestController
@RequestMapping("/api")
public class SubFamilyResource {

    private final Logger log = LoggerFactory.getLogger(SubFamilyResource.class);

    private static final String ENTITY_NAME = "subFamily";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubFamilyService subFamilyService;

    private final SubFamilyRepository subFamilyRepository;

    private final SubFamilyQueryService subFamilyQueryService;

    public SubFamilyResource(
        SubFamilyService subFamilyService,
        SubFamilyRepository subFamilyRepository,
        SubFamilyQueryService subFamilyQueryService
    ) {
        this.subFamilyService = subFamilyService;
        this.subFamilyRepository = subFamilyRepository;
        this.subFamilyQueryService = subFamilyQueryService;
    }

    /**
     * {@code POST  /sub-families} : Create a new subFamily.
     *
     * @param subFamily the subFamily to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subFamily, or with status {@code 400 (Bad Request)} if the subFamily has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sub-families")
    public ResponseEntity<SubFamily> createSubFamily(@Valid @RequestBody SubFamily subFamily) throws URISyntaxException {
        log.debug("REST request to save SubFamily : {}", subFamily);
        if (subFamily.getId() != null) {
            throw new BadRequestAlertException("A new subFamily cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubFamily result = subFamilyService.save(subFamily);
        return ResponseEntity
            .created(new URI("/api/sub-families/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-families/:id} : Updates an existing subFamily.
     *
     * @param id the id of the subFamily to save.
     * @param subFamily the subFamily to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subFamily,
     * or with status {@code 400 (Bad Request)} if the subFamily is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subFamily couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sub-families/{id}")
    public ResponseEntity<SubFamily> updateSubFamily(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubFamily subFamily
    ) throws URISyntaxException {
        log.debug("REST request to update SubFamily : {}, {}", id, subFamily);
        if (subFamily.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subFamily.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subFamilyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubFamily result = subFamilyService.save(subFamily);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subFamily.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sub-families/:id} : Partial updates given fields of an existing subFamily, field will ignore if it is null
     *
     * @param id the id of the subFamily to save.
     * @param subFamily the subFamily to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subFamily,
     * or with status {@code 400 (Bad Request)} if the subFamily is not valid,
     * or with status {@code 404 (Not Found)} if the subFamily is not found,
     * or with status {@code 500 (Internal Server Error)} if the subFamily couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sub-families/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubFamily> partialUpdateSubFamily(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubFamily subFamily
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubFamily partially : {}, {}", id, subFamily);
        if (subFamily.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subFamily.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subFamilyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubFamily> result = subFamilyService.partialUpdate(subFamily);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subFamily.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-families} : get all the subFamilies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subFamilies in body.
     */
    @GetMapping("/sub-families")
    public ResponseEntity<List<SubFamily>> getAllSubFamilies(SubFamilyCriteria criteria) {
        log.debug("REST request to get SubFamilies by criteria: {}", criteria);
        List<SubFamily> entityList = subFamilyQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /sub-families/count} : count all the subFamilies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sub-families/count")
    public ResponseEntity<Long> countSubFamilies(SubFamilyCriteria criteria) {
        log.debug("REST request to count SubFamilies by criteria: {}", criteria);
        return ResponseEntity.ok().body(subFamilyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sub-families/:id} : get the "id" subFamily.
     *
     * @param id the id of the subFamily to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subFamily, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sub-families/{id}")
    public ResponseEntity<SubFamily> getSubFamily(@PathVariable Long id) {
        log.debug("REST request to get SubFamily : {}", id);
        Optional<SubFamily> subFamily = subFamilyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subFamily);
    }

    /**
     * {@code DELETE  /sub-families/:id} : delete the "id" subFamily.
     *
     * @param id the id of the subFamily to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sub-families/{id}")
    public ResponseEntity<Void> deleteSubFamily(@PathVariable Long id) {
        log.debug("REST request to delete SubFamily : {}", id);
        subFamilyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
