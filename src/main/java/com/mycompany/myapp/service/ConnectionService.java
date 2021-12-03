package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Connection;
import com.mycompany.myapp.repository.ConnectionRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Connection}.
 */
@Service
@Transactional
public class ConnectionService {

    private final Logger log = LoggerFactory.getLogger(ConnectionService.class);

    private final ConnectionRepository connectionRepository;

    public ConnectionService(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    /**
     * Save a connection.
     *
     * @param connection the entity to save.
     * @return the persisted entity.
     */
    public Connection save(Connection connection) {
        log.debug("Request to save Connection : {}", connection);
        return connectionRepository.save(connection);
    }

    /**
     * Partially update a connection.
     *
     * @param connection the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Connection> partialUpdate(Connection connection) {
        log.debug("Request to partially update Connection : {}", connection);

        return connectionRepository
            .findById(connection.getId())
            .map(existingConnection -> {
                if (connection.getUsername() != null) {
                    existingConnection.setUsername(connection.getUsername());
                }
                if (connection.getPassword() != null) {
                    existingConnection.setPassword(connection.getPassword());
                }

                return existingConnection;
            })
            .map(connectionRepository::save);
    }

    /**
     * Get all the connections.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Connection> findAll() {
        log.debug("Request to get all Connections");
        return connectionRepository.findAll();
    }

    /**
     * Get one connection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Connection> findOne(Long id) {
        log.debug("Request to get Connection : {}", id);
        return connectionRepository.findById(id);
    }

    /**
     * Delete the connection by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Connection : {}", id);
        connectionRepository.deleteById(id);
    }
}
