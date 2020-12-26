package com.andrew.rental.service;

import com.andrew.rental.domain.Type;
import com.andrew.rental.repository.TypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Type}.
 */
@Service
@Transactional
public class TypeService {

    private final Logger log = LoggerFactory.getLogger(TypeService.class);

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    /**
     * Save a type.
     *
     * @param type the entity to save.
     * @return the persisted entity.
     */
    public Type save(Type type) {
        log.debug("Request to save Type : {}", type);
        return typeRepository.save(type);
    }

    /**
     * Get all the types.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Type> findAll() {
        log.debug("Request to get all Types");
        return typeRepository.findAll();
    }


    /**
     * Get one type by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Type> findOne(Long id) {
        log.debug("Request to get Type : {}", id);
        return typeRepository.findById(id);
    }

    /**
     * Delete the type by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Type : {}", id);
        typeRepository.deleteById(id);
    }
}
