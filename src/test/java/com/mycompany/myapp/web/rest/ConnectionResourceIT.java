package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Connection;
import com.mycompany.myapp.repository.ConnectionRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConnectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConnectionResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/connections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConnectionMockMvc;

    private Connection connection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Connection createEntity(EntityManager em) {
        Connection connection = new Connection().username(DEFAULT_USERNAME).password(DEFAULT_PASSWORD);
        return connection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Connection createUpdatedEntity(EntityManager em) {
        Connection connection = new Connection().username(UPDATED_USERNAME).password(UPDATED_PASSWORD);
        return connection;
    }

    @BeforeEach
    public void initTest() {
        connection = createEntity(em);
    }

    @Test
    @Transactional
    void createConnection() throws Exception {
        int databaseSizeBeforeCreate = connectionRepository.findAll().size();
        // Create the Connection
        restConnectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(connection)))
            .andExpect(status().isCreated());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeCreate + 1);
        Connection testConnection = connectionList.get(connectionList.size() - 1);
        assertThat(testConnection.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testConnection.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createConnectionWithExistingId() throws Exception {
        // Create the Connection with an existing ID
        connection.setId(1L);

        int databaseSizeBeforeCreate = connectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConnectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(connection)))
            .andExpect(status().isBadRequest());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = connectionRepository.findAll().size();
        // set the field null
        connection.setUsername(null);

        // Create the Connection, which fails.

        restConnectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(connection)))
            .andExpect(status().isBadRequest());

        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = connectionRepository.findAll().size();
        // set the field null
        connection.setPassword(null);

        // Create the Connection, which fails.

        restConnectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(connection)))
            .andExpect(status().isBadRequest());

        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConnections() throws Exception {
        // Initialize the database
        connectionRepository.saveAndFlush(connection);

        // Get all the connectionList
        restConnectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(connection.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getConnection() throws Exception {
        // Initialize the database
        connectionRepository.saveAndFlush(connection);

        // Get the connection
        restConnectionMockMvc
            .perform(get(ENTITY_API_URL_ID, connection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(connection.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingConnection() throws Exception {
        // Get the connection
        restConnectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConnection() throws Exception {
        // Initialize the database
        connectionRepository.saveAndFlush(connection);

        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();

        // Update the connection
        Connection updatedConnection = connectionRepository.findById(connection.getId()).get();
        // Disconnect from session so that the updates on updatedConnection are not directly saved in db
        em.detach(updatedConnection);
        updatedConnection.username(UPDATED_USERNAME).password(UPDATED_PASSWORD);

        restConnectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConnection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConnection))
            )
            .andExpect(status().isOk());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
        Connection testConnection = connectionList.get(connectionList.size() - 1);
        assertThat(testConnection.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testConnection.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingConnection() throws Exception {
        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();
        connection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConnectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, connection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(connection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConnection() throws Exception {
        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();
        connection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(connection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConnection() throws Exception {
        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();
        connection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(connection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConnectionWithPatch() throws Exception {
        // Initialize the database
        connectionRepository.saveAndFlush(connection);

        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();

        // Update the connection using partial update
        Connection partialUpdatedConnection = new Connection();
        partialUpdatedConnection.setId(connection.getId());

        partialUpdatedConnection.username(UPDATED_USERNAME).password(UPDATED_PASSWORD);

        restConnectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConnection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConnection))
            )
            .andExpect(status().isOk());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
        Connection testConnection = connectionList.get(connectionList.size() - 1);
        assertThat(testConnection.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testConnection.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateConnectionWithPatch() throws Exception {
        // Initialize the database
        connectionRepository.saveAndFlush(connection);

        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();

        // Update the connection using partial update
        Connection partialUpdatedConnection = new Connection();
        partialUpdatedConnection.setId(connection.getId());

        partialUpdatedConnection.username(UPDATED_USERNAME).password(UPDATED_PASSWORD);

        restConnectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConnection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConnection))
            )
            .andExpect(status().isOk());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
        Connection testConnection = connectionList.get(connectionList.size() - 1);
        assertThat(testConnection.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testConnection.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingConnection() throws Exception {
        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();
        connection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConnectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, connection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(connection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConnection() throws Exception {
        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();
        connection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(connection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConnection() throws Exception {
        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();
        connection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnectionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(connection))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConnection() throws Exception {
        // Initialize the database
        connectionRepository.saveAndFlush(connection);

        int databaseSizeBeforeDelete = connectionRepository.findAll().size();

        // Delete the connection
        restConnectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, connection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
