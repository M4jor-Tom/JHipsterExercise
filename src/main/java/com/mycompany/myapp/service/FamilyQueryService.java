package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Family;
import com.mycompany.myapp.repository.FamilyRepository;
import com.mycompany.myapp.service.criteria.FamilyCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Family} entities in the database.
 * The main input is a {@link FamilyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Family} or a {@link Page} of {@link Family} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FamilyQueryService extends QueryService<Family> {

    private final Logger log = LoggerFactory.getLogger(FamilyQueryService.class);

    private final FamilyRepository familyRepository;

    public FamilyQueryService(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    /**
     * Return a {@link List} of {@link Family} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Family> findByCriteria(FamilyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Family> specification = createSpecification(criteria);
        return familyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Family} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Family> findByCriteria(FamilyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Family> specification = createSpecification(criteria);
        return familyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FamilyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Family> specification = createSpecification(criteria);
        return familyRepository.count(specification);
    }

    /**
     * Function to convert {@link FamilyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Family> createSpecification(FamilyCriteria criteria) {
        Specification<Family> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Family_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Family_.name));
            }
        }
        return specification;
    }
}
