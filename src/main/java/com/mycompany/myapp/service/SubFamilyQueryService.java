package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.SubFamily;
import com.mycompany.myapp.repository.SubFamilyRepository;
import com.mycompany.myapp.service.criteria.SubFamilyCriteria;
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
 * Service for executing complex queries for {@link SubFamily} entities in the database.
 * The main input is a {@link SubFamilyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubFamily} or a {@link Page} of {@link SubFamily} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubFamilyQueryService extends QueryService<SubFamily> {

    private final Logger log = LoggerFactory.getLogger(SubFamilyQueryService.class);

    private final SubFamilyRepository subFamilyRepository;

    public SubFamilyQueryService(SubFamilyRepository subFamilyRepository) {
        this.subFamilyRepository = subFamilyRepository;
    }

    /**
     * Return a {@link List} of {@link SubFamily} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubFamily> findByCriteria(SubFamilyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubFamily> specification = createSpecification(criteria);
        return subFamilyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SubFamily} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubFamily> findByCriteria(SubFamilyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubFamily> specification = createSpecification(criteria);
        return subFamilyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubFamilyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubFamily> specification = createSpecification(criteria);
        return subFamilyRepository.count(specification);
    }

    /**
     * Function to convert {@link SubFamilyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubFamily> createSpecification(SubFamilyCriteria criteria) {
        Specification<SubFamily> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubFamily_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SubFamily_.name));
            }
            if (criteria.getFamilyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFamilyId(), root -> root.join(SubFamily_.family, JoinType.LEFT).get(Family_.id))
                    );
            }
        }
        return specification;
    }
}
