package com.andrew.rental.service;

import com.andrew.rental.domain.ActiveRent;
import com.andrew.rental.repository.ActiveRentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ActiveRent}.
 */
@Service
@Transactional
public class ActiveRentService {

    private final Logger log = LoggerFactory.getLogger(ActiveRentService.class);

    private final ActiveRentRepository activeRentRepository;

    public ActiveRentService(ActiveRentRepository activeRentRepository) {
        this.activeRentRepository = activeRentRepository;
    }

    /**
     * Save a activeRent.
     *
     * @param activeRent the entity to save.
     * @return the persisted entity.
     */
    public ActiveRent save(ActiveRent activeRent) {
        log.debug("Request to save ActiveRent : {}", activeRent);
        return activeRentRepository.save(activeRent);
    }

    /**
     * Get all the activeRents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ActiveRent> findAll(Pageable pageable) {
        log.debug("Request to get all ActiveRents");
        return activeRentRepository.findAll(pageable);
    }


    /**
     * Get one activeRent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActiveRent> findOne(Long id) {
        log.debug("Request to get ActiveRent : {}", id);
        return activeRentRepository.findById(id);
    }

    /**
     * Delete the activeRent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ActiveRent : {}", id);
        activeRentRepository.deleteById(id);
    }
}
