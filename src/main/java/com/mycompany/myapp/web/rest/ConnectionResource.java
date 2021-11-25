package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Connection;
import com.mycompany.myapp.repository.ConnectionRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Connection}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConnectionResource {

    private final Logger log = LoggerFactory.getLogger(ConnectionResource.class);

    private static final String ENTITY_NAME = "connection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConnectionRepository connectionRepository;

    public ConnectionResource(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    /**
     * {@code POST  /connections} : Create a new connection.
     *
     * @param connection the connection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new connection, or with status {@code 400 (Bad Request)} if the connection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/connections")
    public ResponseEntity<Connection> createConnection(@RequestBody Connection connection) throws URISyntaxException {
        log.debug("REST request to save Connection : {}", connection);
        if (connection.getId() != null) {
            throw new BadRequestAlertException("A new connection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Connection result = connectionRepository.save(connection);
        return ResponseEntity
            .created(new URI("/api/connections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /connections/:id} : Updates an existing connection.
     *
     * @param id the id of the connection to save.
     * @param connection the connection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated connection,
     * or with status {@code 400 (Bad Request)} if the connection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the connection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/connections/{id}")
    public ResponseEntity<Connection> updateConnection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Connection connection
    ) throws URISyntaxException {
        log.debug("REST request to update Connection : {}, {}", id, connection);
        if (connection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, connection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!connectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Connection result = connectionRepository.save(connection);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, connection.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /connections/:id} : Partial updates given fields of an existing connection, field will ignore if it is null
     *
     * @param id the id of the connection to save.
     * @param connection the connection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated connection,
     * or with status {@code 400 (Bad Request)} if the connection is not valid,
     * or with status {@code 404 (Not Found)} if the connection is not found,
     * or with status {@code 500 (Internal Server Error)} if the connection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/connections/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Connection> partialUpdateConnection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Connection connection
    ) throws URISyntaxException {
        log.debug("REST request to partial update Connection partially : {}, {}", id, connection);
        if (connection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, connection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!connectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Connection> result = connectionRepository
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

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, connection.getId().toString())
        );
    }

    /**
     * {@code GET  /connections} : get all the connections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of connections in body.
     */
    @GetMapping("/connections")
    public List<Connection> getAllConnections() {
        log.debug("REST request to get all Connections");
        return connectionRepository.findAll();
    }

    /**
     * {@code GET  /connections/:id} : get the "id" connection.
     *
     * @param id the id of the connection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the connection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/connections/{id}")
    public ResponseEntity<Connection> getConnection(@PathVariable Long id) {
        log.debug("REST request to get Connection : {}", id);
        Optional<Connection> connection = connectionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(connection);
    }

    /**
     * {@code DELETE  /connections/:id} : delete the "id" connection.
     *
     * @param id the id of the connection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/connections/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Long id) {
        log.debug("REST request to delete Connection : {}", id);
        connectionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
