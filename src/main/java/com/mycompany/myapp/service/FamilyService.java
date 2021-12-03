package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Family;
import com.mycompany.myapp.repository.FamilyRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Family}.
 */
@Service
@Transactional
public class FamilyService {

    private final Logger log = LoggerFactory.getLogger(FamilyService.class);

    private final FamilyRepository familyRepository;

    public FamilyService(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    /**
     * Save a family.
     *
     * @param family the entity to save.
     * @return the persisted entity.
     */
    public Family save(Family family) {
        log.debug("Request to save Family : {}", family);
        return familyRepository.save(family);
    }

    /**
     * Partially update a family.
     *
     * @param family the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Family> partialUpdate(Family family) {
        log.debug("Request to partially update Family : {}", family);

        return familyRepository
            .findById(family.getId())
            .map(existingFamily -> {
                if (family.getName() != null) {
                    existingFamily.setName(family.getName());
                }

                return existingFamily;
            })
            .map(familyRepository::save);
    }

    /**
     * Get all the families.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Family> findAll() {
        log.debug("Request to get all Families");
        return familyRepository.findAll();
    }

    /**
     * Get one family by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Family> findOne(Long id) {
        log.debug("Request to get Family : {}", id);
        return familyRepository.findById(id);
    }

    /**
     * Delete the family by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Family : {}", id);
        familyRepository.deleteById(id);
    }
}
