package com.andrew.rental.service;

import com.andrew.rental.domain.Model;
import com.andrew.rental.repository.ModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Model}.
 */
@Service
@Transactional
public class ModelService {

    private final Logger log = LoggerFactory.getLogger(ModelService.class);

    private final ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    /**
     * Save a model.
     *
     * @param model the entity to save.
     * @return the persisted entity.
     */
    public Model save(Model model) {
        log.debug("Request to save Model : {}", model);
        return modelRepository.save(model);
    }

    /**
     * Get all the models.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Model> findAll() {
        log.debug("Request to get all Models");
        return modelRepository.findAll();
    }


    /**
     * Get one model by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Model> findOne(Long id) {
        log.debug("Request to get Model : {}", id);
        return modelRepository.findById(id);
    }

    /**
     * Delete the model by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Model : {}", id);
        modelRepository.deleteById(id);
    }
}
