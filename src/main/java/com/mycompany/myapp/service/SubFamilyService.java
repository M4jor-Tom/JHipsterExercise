package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SubFamily;
import com.mycompany.myapp.repository.SubFamilyRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SubFamily}.
 */
@Service
@Transactional
public class SubFamilyService {

    private final Logger log = LoggerFactory.getLogger(SubFamilyService.class);

    private final SubFamilyRepository subFamilyRepository;

    public SubFamilyService(SubFamilyRepository subFamilyRepository) {
        this.subFamilyRepository = subFamilyRepository;
    }

    /**
     * Save a subFamily.
     *
     * @param subFamily the entity to save.
     * @return the persisted entity.
     */
    public SubFamily save(SubFamily subFamily) {
        log.debug("Request to save SubFamily : {}", subFamily);
        return subFamilyRepository.save(subFamily);
    }

    /**
     * Partially update a subFamily.
     *
     * @param subFamily the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubFamily> partialUpdate(SubFamily subFamily) {
        log.debug("Request to partially update SubFamily : {}", subFamily);

        return subFamilyRepository
            .findById(subFamily.getId())
            .map(existingSubFamily -> {
                if (subFamily.getName() != null) {
                    existingSubFamily.setName(subFamily.getName());
                }

                return existingSubFamily;
            })
            .map(subFamilyRepository::save);
    }

    /**
     * Get all the subFamilies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubFamily> findAll() {
        log.debug("Request to get all SubFamilies");
        return subFamilyRepository.findAll();
    }

    /**
     * Get one subFamily by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubFamily> findOne(Long id) {
        log.debug("Request to get SubFamily : {}", id);
        return subFamilyRepository.findById(id);
    }

    /**
     * Delete the subFamily by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubFamily : {}", id);
        subFamilyRepository.deleteById(id);
    }
}
