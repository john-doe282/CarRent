package com.andrew.rental.controller;

import com.andrew.rental.controller.errors.BadRequestAlertException;
import com.andrew.rental.domain.Model;
import com.andrew.rental.security.AuthoritiesConstants;
import com.andrew.rental.service.ModelService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.andrew.rental.domain.Model}.
 */
@RestController
@RequestMapping("/api")
public class ModelResource {

    private final Logger log = LoggerFactory.getLogger(ModelResource.class);

    private static final String ENTITY_NAME = "model";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModelService modelService;

    public ModelResource(ModelService modelService) {
        this.modelService = modelService;
    }

    /**
     * {@code POST  /models} : Create a new model.
     *
     * @param model the model to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new model, or with status {@code 400 (Bad Request)} if the model has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/models")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Model> createModel(@Valid @RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to save Model : {}", model);
        if (model.getId() != null) {
            throw new BadRequestAlertException("A new model cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Model result = modelService.save(model);
        return ResponseEntity.created(new URI("/api/models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /models} : Updates an existing model.
     *
     * @param model the model to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated model,
     * or with status {@code 400 (Bad Request)} if the model is not valid,
     * or with status {@code 500 (Internal Server Error)} if the model couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/models")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Model> updateModel(@Valid @RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to update Model : {}", model);
        if (model.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Model result = modelService.save(model);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, model.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /models} : get all the models.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
    @GetMapping("/models")
    public List<Model> getAllModels() {
        log.debug("REST request to get all Models");
        return modelService.findAll();
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
        Optional<Model> model = modelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(model);
    }

    /**
     * {@code DELETE  /models/:id} : delete the "id" model.
     *
     * @param id the id of the model to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/models/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        log.debug("REST request to delete Model : {}", id);
        modelService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
