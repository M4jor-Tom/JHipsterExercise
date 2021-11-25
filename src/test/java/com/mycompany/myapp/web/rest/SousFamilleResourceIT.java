package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SousFamille;
import com.mycompany.myapp.repository.SousFamilleRepository;
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
 * Integration tests for the {@link SousFamilleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousFamilleResourceIT {

    private static final Integer DEFAULT_I_D_SOUS_FAMILLE = 1;
    private static final Integer UPDATED_I_D_SOUS_FAMILLE = 2;

    private static final String DEFAULT_SOUS_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-familles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousFamilleRepository sousFamilleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousFamilleMockMvc;

    private SousFamille sousFamille;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousFamille createEntity(EntityManager em) {
        SousFamille sousFamille = new SousFamille().iDSousFamille(DEFAULT_I_D_SOUS_FAMILLE).sousFamille(DEFAULT_SOUS_FAMILLE);
        return sousFamille;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousFamille createUpdatedEntity(EntityManager em) {
        SousFamille sousFamille = new SousFamille().iDSousFamille(UPDATED_I_D_SOUS_FAMILLE).sousFamille(UPDATED_SOUS_FAMILLE);
        return sousFamille;
    }

    @BeforeEach
    public void initTest() {
        sousFamille = createEntity(em);
    }

    @Test
    @Transactional
    void createSousFamille() throws Exception {
        int databaseSizeBeforeCreate = sousFamilleRepository.findAll().size();
        // Create the SousFamille
        restSousFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousFamille)))
            .andExpect(status().isCreated());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeCreate + 1);
        SousFamille testSousFamille = sousFamilleList.get(sousFamilleList.size() - 1);
        assertThat(testSousFamille.getiDSousFamille()).isEqualTo(DEFAULT_I_D_SOUS_FAMILLE);
        assertThat(testSousFamille.getSousFamille()).isEqualTo(DEFAULT_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void createSousFamilleWithExistingId() throws Exception {
        // Create the SousFamille with an existing ID
        sousFamille.setId(1L);

        int databaseSizeBeforeCreate = sousFamilleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousFamille)))
            .andExpect(status().isBadRequest());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSousFamilles() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList
        restSousFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousFamille.getId().intValue())))
            .andExpect(jsonPath("$.[*].iDSousFamille").value(hasItem(DEFAULT_I_D_SOUS_FAMILLE)))
            .andExpect(jsonPath("$.[*].sousFamille").value(hasItem(DEFAULT_SOUS_FAMILLE)));
    }

    @Test
    @Transactional
    void getSousFamille() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get the sousFamille
        restSousFamilleMockMvc
            .perform(get(ENTITY_API_URL_ID, sousFamille.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousFamille.getId().intValue()))
            .andExpect(jsonPath("$.iDSousFamille").value(DEFAULT_I_D_SOUS_FAMILLE))
            .andExpect(jsonPath("$.sousFamille").value(DEFAULT_SOUS_FAMILLE));
    }

    @Test
    @Transactional
    void getNonExistingSousFamille() throws Exception {
        // Get the sousFamille
        restSousFamilleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousFamille() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();

        // Update the sousFamille
        SousFamille updatedSousFamille = sousFamilleRepository.findById(sousFamille.getId()).get();
        // Disconnect from session so that the updates on updatedSousFamille are not directly saved in db
        em.detach(updatedSousFamille);
        updatedSousFamille.iDSousFamille(UPDATED_I_D_SOUS_FAMILLE).sousFamille(UPDATED_SOUS_FAMILLE);

        restSousFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousFamille.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousFamille))
            )
            .andExpect(status().isOk());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
        SousFamille testSousFamille = sousFamilleList.get(sousFamilleList.size() - 1);
        assertThat(testSousFamille.getiDSousFamille()).isEqualTo(UPDATED_I_D_SOUS_FAMILLE);
        assertThat(testSousFamille.getSousFamille()).isEqualTo(UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void putNonExistingSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousFamille.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousFamille)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousFamilleWithPatch() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();

        // Update the sousFamille using partial update
        SousFamille partialUpdatedSousFamille = new SousFamille();
        partialUpdatedSousFamille.setId(sousFamille.getId());

        partialUpdatedSousFamille.iDSousFamille(UPDATED_I_D_SOUS_FAMILLE).sousFamille(UPDATED_SOUS_FAMILLE);

        restSousFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousFamille))
            )
            .andExpect(status().isOk());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
        SousFamille testSousFamille = sousFamilleList.get(sousFamilleList.size() - 1);
        assertThat(testSousFamille.getiDSousFamille()).isEqualTo(UPDATED_I_D_SOUS_FAMILLE);
        assertThat(testSousFamille.getSousFamille()).isEqualTo(UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void fullUpdateSousFamilleWithPatch() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();

        // Update the sousFamille using partial update
        SousFamille partialUpdatedSousFamille = new SousFamille();
        partialUpdatedSousFamille.setId(sousFamille.getId());

        partialUpdatedSousFamille.iDSousFamille(UPDATED_I_D_SOUS_FAMILLE).sousFamille(UPDATED_SOUS_FAMILLE);

        restSousFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousFamille))
            )
            .andExpect(status().isOk());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
        SousFamille testSousFamille = sousFamilleList.get(sousFamilleList.size() - 1);
        assertThat(testSousFamille.getiDSousFamille()).isEqualTo(UPDATED_I_D_SOUS_FAMILLE);
        assertThat(testSousFamille.getSousFamille()).isEqualTo(UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void patchNonExistingSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousFamille))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousFamille() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        int databaseSizeBeforeDelete = sousFamilleRepository.findAll().size();

        // Delete the sousFamille
        restSousFamilleMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousFamille.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
