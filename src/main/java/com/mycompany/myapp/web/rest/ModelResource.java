package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Model;
import com.mycompany.myapp.repository.ModelRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Model}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ModelResource {

    private final Logger log = LoggerFactory.getLogger(ModelResource.class);

    private static final String ENTITY_NAME = "model";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModelRepository modelRepository;

    public ModelResource(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    /**
     * {@code POST  /models} : Create a new model.
     *
     * @param model the model to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new model, or with status {@code 400 (Bad Request)} if the model has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/models")
    public ResponseEntity<Model> createModel(@RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to save Model : {}", model);
        if (model.getId() != null) {
            throw new BadRequestAlertException("A new model cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Model result = modelRepository.save(model);
        return ResponseEntity
            .created(new URI("/api/models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /models/:id} : Updates an existing model.
     *
     * @param id the id of the model to save.
     * @param model the model to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated model,
     * or with status {@code 400 (Bad Request)} if the model is not valid,
     * or with status {@code 500 (Internal Server Error)} if the model couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/models/{id}")
    public ResponseEntity<Model> updateModel(@PathVariable(value = "id", required = false) final Long id, @RequestBody Model model)
        throws URISyntaxException {
        log.debug("REST request to update Model : {}, {}", id, model);
        if (model.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, model.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Model result = modelRepository.save(model);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, model.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /models/:id} : Partial updates given fields of an existing model, field will ignore if it is null
     *
     * @param id the id of the model to save.
     * @param model the model to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated model,
     * or with status {@code 400 (Bad Request)} if the model is not valid,
     * or with status {@code 404 (Not Found)} if the model is not found,
     * or with status {@code 500 (Internal Server Error)} if the model couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/models/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Model> partialUpdateModel(@PathVariable(value = "id", required = false) final Long id, @RequestBody Model model)
        throws URISyntaxException {
        log.debug("REST request to partial update Model partially : {}, {}", id, model);
        if (model.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, model.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Model> result = modelRepository
            .findById(model.getId())
            .map(existingModel -> {
                if (model.getName() != null) {
                    existingModel.setName(model.getName());
                }

                return existingModel;
            })
            .map(modelRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, model.getId().toString())
        );
    }

    /**
     * {@code GET  /models} : get all the models.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
    @GetMapping("/models")
    public List<Model> getAllModels() {
        log.debug("REST request to get all Models");
        return modelRepository.findAll();
    }

    /**
     * {@code GET  /models/:id} : get the "id" model.
     *
     * @param id the id of the model to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the model, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/models/{id}")
    public ResponseEntity<Model> getModel(@PathVariable Long id) {
        log.debug("REST request to get Model : {}", id);
        Optional<Model> model = modelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(model);
    }

    /**
     * {@code DELETE  /models/:id} : delete the "id" model.
     *
     * @param id the id of the model to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/models/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        log.debug("REST request to delete Model : {}", id);
        modelRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
