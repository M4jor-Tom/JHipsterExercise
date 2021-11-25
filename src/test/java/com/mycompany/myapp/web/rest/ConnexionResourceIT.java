package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Connexion;
import com.mycompany.myapp.repository.ConnexionRepository;
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
 * Integration tests for the {@link ConnexionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConnexionResourceIT {

    private static final Integer DEFAULT_I_D_CONNEXION = 1;
    private static final Integer UPDATED_I_D_CONNEXION = 2;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/connexions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConnexionRepository connexionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConnexionMockMvc;

    private Connexion connexion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Connexion createEntity(EntityManager em) {
        Connexion connexion = new Connexion().iDConnexion(DEFAULT_I_D_CONNEXION).username(DEFAULT_USERNAME).password(DEFAULT_PASSWORD);
        return connexion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Connexion createUpdatedEntity(EntityManager em) {
        Connexion connexion = new Connexion().iDConnexion(UPDATED_I_D_CONNEXION).username(UPDATED_USERNAME).password(UPDATED_PASSWORD);
        return connexion;
    }

    @BeforeEach
    public void initTest() {
        connexion = createEntity(em);
    }

    @Test
    @Transactional
    void createConnexion() throws Exception {
        int databaseSizeBeforeCreate = connexionRepository.findAll().size();
        // Create the Connexion
        restConnexionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(connexion)))
            .andExpect(status().isCreated());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeCreate + 1);
        Connexion testConnexion = connexionList.get(connexionList.size() - 1);
        assertThat(testConnexion.getiDConnexion()).isEqualTo(DEFAULT_I_D_CONNEXION);
        assertThat(testConnexion.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testConnexion.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createConnexionWithExistingId() throws Exception {
        // Create the Connexion with an existing ID
        connexion.setId(1L);

        int databaseSizeBeforeCreate = connexionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConnexionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(connexion)))
            .andExpect(status().isBadRequest());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConnexions() throws Exception {
        // Initialize the database
        connexionRepository.saveAndFlush(connexion);

        // Get all the connexionList
        restConnexionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(connexion.getId().intValue())))
            .andExpect(jsonPath("$.[*].iDConnexion").value(hasItem(DEFAULT_I_D_CONNEXION)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getConnexion() throws Exception {
        // Initialize the database
        connexionRepository.saveAndFlush(connexion);

        // Get the connexion
        restConnexionMockMvc
            .perform(get(ENTITY_API_URL_ID, connexion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(connexion.getId().intValue()))
            .andExpect(jsonPath("$.iDConnexion").value(DEFAULT_I_D_CONNEXION))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingConnexion() throws Exception {
        // Get the connexion
        restConnexionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConnexion() throws Exception {
        // Initialize the database
        connexionRepository.saveAndFlush(connexion);

        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();

        // Update the connexion
        Connexion updatedConnexion = connexionRepository.findById(connexion.getId()).get();
        // Disconnect from session so that the updates on updatedConnexion are not directly saved in db
        em.detach(updatedConnexion);
        updatedConnexion.iDConnexion(UPDATED_I_D_CONNEXION).username(UPDATED_USERNAME).password(UPDATED_PASSWORD);

        restConnexionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConnexion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConnexion))
            )
            .andExpect(status().isOk());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
        Connexion testConnexion = connexionList.get(connexionList.size() - 1);
        assertThat(testConnexion.getiDConnexion()).isEqualTo(UPDATED_I_D_CONNEXION);
        assertThat(testConnexion.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testConnexion.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingConnexion() throws Exception {
        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();
        connexion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConnexionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, connexion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(connexion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConnexion() throws Exception {
        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();
        connexion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnexionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(connexion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConnexion() throws Exception {
        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();
        connexion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnexionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(connexion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConnexionWithPatch() throws Exception {
        // Initialize the database
        connexionRepository.saveAndFlush(connexion);

        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();

        // Update the connexion using partial update
        Connexion partialUpdatedConnexion = new Connexion();
        partialUpdatedConnexion.setId(connexion.getId());

        partialUpdatedConnexion.password(UPDATED_PASSWORD);

        restConnexionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConnexion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConnexion))
            )
            .andExpect(status().isOk());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
        Connexion testConnexion = connexionList.get(connexionList.size() - 1);
        assertThat(testConnexion.getiDConnexion()).isEqualTo(DEFAULT_I_D_CONNEXION);
        assertThat(testConnexion.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testConnexion.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateConnexionWithPatch() throws Exception {
        // Initialize the database
        connexionRepository.saveAndFlush(connexion);

        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();

        // Update the connexion using partial update
        Connexion partialUpdatedConnexion = new Connexion();
        partialUpdatedConnexion.setId(connexion.getId());

        partialUpdatedConnexion.iDConnexion(UPDATED_I_D_CONNEXION).username(UPDATED_USERNAME).password(UPDATED_PASSWORD);

        restConnexionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConnexion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConnexion))
            )
            .andExpect(status().isOk());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
        Connexion testConnexion = connexionList.get(connexionList.size() - 1);
        assertThat(testConnexion.getiDConnexion()).isEqualTo(UPDATED_I_D_CONNEXION);
        assertThat(testConnexion.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testConnexion.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingConnexion() throws Exception {
        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();
        connexion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConnexionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, connexion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(connexion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConnexion() throws Exception {
        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();
        connexion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnexionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(connexion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConnexion() throws Exception {
        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();
        connexion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnexionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(connexion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConnexion() throws Exception {
        // Initialize the database
        connexionRepository.saveAndFlush(connexion);

        int databaseSizeBeforeDelete = connexionRepository.findAll().size();

        // Delete the connexion
        restConnexionMockMvc
            .perform(delete(ENTITY_API_URL_ID, connexion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
