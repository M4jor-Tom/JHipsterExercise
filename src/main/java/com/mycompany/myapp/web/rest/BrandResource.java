package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Brand;
import com.mycompany.myapp.repository.BrandRepository;
import com.mycompany.myapp.service.BrandQueryService;
import com.mycompany.myapp.service.BrandService;
import com.mycompany.myapp.service.criteria.BrandCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Brand}.
 */
@RestController
@RequestMapping("/api")
public class BrandResource {

    private final Logger log = LoggerFactory.getLogger(BrandResource.class);

    private static final String ENTITY_NAME = "brand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BrandService brandService;

    private final BrandRepository brandRepository;

    private final BrandQueryService brandQueryService;

    public BrandResource(BrandService brandService, BrandRepository brandRepository, BrandQueryService brandQueryService) {
        this.brandService = brandService;
        this.brandRepository = brandRepository;
        this.brandQueryService = brandQueryService;
    }

    /**
     * {@code POST  /brands} : Create a new brand.
     *
     * @param brand the brand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new brand, or with status {@code 400 (Bad Request)} if the brand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/brands")
    public ResponseEntity<Brand> createBrand(@Valid @RequestBody Brand brand) throws URISyntaxException {
        log.debug("REST request to save Brand : {}", brand);
        if (brand.getId() != null) {
            throw new BadRequestAlertException("A new brand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Brand result = brandService.save(brand);
        return ResponseEntity
            .created(new URI("/api/brands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /brands/:id} : Updates an existing brand.
     *
     * @param id the id of the brand to save.
     * @param brand the brand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brand,
     * or with status {@code 400 (Bad Request)} if the brand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the brand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/brands/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Brand brand)
        throws URISyntaxException {
        log.debug("REST request to update Brand : {}, {}", id, brand);
        if (brand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, brand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!brandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Brand result = brandService.save(brand);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, brand.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /brands/:id} : Partial updates given fields of an existing brand, field will ignore if it is null
     *
     * @param id the id of the brand to save.
     * @param brand the brand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brand,
     * or with status {@code 400 (Bad Request)} if the brand is not valid,
     * or with status {@code 404 (Not Found)} if the brand is not found,
     * or with status {@code 500 (Internal Server Error)} if the brand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/brands/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Brand> partialUpdateBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Brand brand
    ) throws URISyntaxException {
        log.debug("REST request to partial update Brand partially : {}, {}", id, brand);
        if (brand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, brand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!brandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Brand> result = brandService.partialUpdate(brand);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, brand.getId().toString())
        );
    }

    /**
     * {@code GET  /brands} : get all the brands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of brands in body.
     */
    @GetMapping("/brands")
    public ResponseEntity<List<Brand>> getAllBrands(BrandCriteria criteria) {
        log.debug("REST request to get Brands by criteria: {}", criteria);
        List<Brand> entityList = brandQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /brands/count} : count all the brands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/brands/count")
    public ResponseEntity<Long> countBrands(BrandCriteria criteria) {
        log.debug("REST request to count Brands by criteria: {}", criteria);
        return ResponseEntity.ok().body(brandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /brands/:id} : get the "id" brand.
     *
     * @param id the id of the brand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the brand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/brands/{id}")
    public ResponseEntity<Brand> getBrand(@PathVariable Long id) {
        log.debug("REST request to get Brand : {}", id);
        Optional<Brand> brand = brandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brand);
    }

    /**
     * {@code DELETE  /brands/:id} : delete the "id" brand.
     *
     * @param id the id of the brand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/brands/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        log.debug("REST request to delete Brand : {}", id);
        brandService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
