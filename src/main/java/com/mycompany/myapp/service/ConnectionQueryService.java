package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Connection;
import com.mycompany.myapp.repository.ConnectionRepository;
import com.mycompany.myapp.service.criteria.ConnectionCriteria;
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
 * Service for executing complex queries for {@link Connection} entities in the database.
 * The main input is a {@link ConnectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Connection} or a {@link Page} of {@link Connection} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConnectionQueryService extends QueryService<Connection> {

    private final Logger log = LoggerFactory.getLogger(ConnectionQueryService.class);

    private final ConnectionRepository connectionRepository;

    public ConnectionQueryService(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    /**
     * Return a {@link List} of {@link Connection} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Connection> findByCriteria(ConnectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Connection> specification = createSpecification(criteria);
        return connectionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Connection} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Connection> findByCriteria(ConnectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Connection> specification = createSpecification(criteria);
        return connectionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConnectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Connection> specification = createSpecification(criteria);
        return connectionRepository.count(specification);
    }

    /**
     * Function to convert {@link ConnectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Connection> createSpecification(ConnectionCriteria criteria) {
        Specification<Connection> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Connection_.id));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), Connection_.username));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Connection_.password));
            }
        }
        return specification;
    }
}
