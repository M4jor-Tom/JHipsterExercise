package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SubFamily;
import com.mycompany.myapp.repository.SubFamilyRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SubFamily}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SubFamilyResource {

    private final Logger log = LoggerFactory.getLogger(SubFamilyResource.class);

    private static final String ENTITY_NAME = "subFamily";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubFamilyRepository subFamilyRepository;

    public SubFamilyResource(SubFamilyRepository subFamilyRepository) {
        this.subFamilyRepository = subFamilyRepository;
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
        SubFamily result = subFamilyRepository.save(subFamily);
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

        SubFamily result = subFamilyRepository.save(subFamily);
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

        Optional<SubFamily> result = subFamilyRepository
            .findById(subFamily.getId())
            .map(existingSubFamily -> {
                if (subFamily.getName() != null) {
                    existingSubFamily.setName(subFamily.getName());
                }

                return existingSubFamily;
            })
            .map(subFamilyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subFamily.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-families} : get all the subFamilies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subFamilies in body.
     */
    @GetMapping("/sub-families")
    public ResponseEntity<List<SubFamily>> getAllSubFamilies(Pageable pageable) {
        log.debug("REST request to get all SubFamilies");
        Page<SubFamily> page = subFamilyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
        Optional<SubFamily> subFamily = subFamilyRepository.findById(id);
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
        subFamilyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
