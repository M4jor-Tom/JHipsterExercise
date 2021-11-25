package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Family;
import com.mycompany.myapp.repository.FamilyRepository;
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
 * Integration tests for the {@link FamilyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamilyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/families";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilyMockMvc;

    private Family family;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Family createEntity(EntityManager em) {
        Family family = new Family().name(DEFAULT_NAME);
        return family;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Family createUpdatedEntity(EntityManager em) {
        Family family = new Family().name(UPDATED_NAME);
        return family;
    }

    @BeforeEach
    public void initTest() {
        family = createEntity(em);
    }

    @Test
    @Transactional
    void createFamily() throws Exception {
        int databaseSizeBeforeCreate = familyRepository.findAll().size();
        // Create the Family
        restFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(family)))
            .andExpect(status().isCreated());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeCreate + 1);
        Family testFamily = familyList.get(familyList.size() - 1);
        assertThat(testFamily.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createFamilyWithExistingId() throws Exception {
        // Create the Family with an existing ID
        family.setId(1L);

        int databaseSizeBeforeCreate = familyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(family)))
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFamilies() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get all the familyList
        restFamilyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(family.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get the family
        restFamilyMockMvc
            .perform(get(ENTITY_API_URL_ID, family.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(family.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingFamily() throws Exception {
        // Get the family
        restFamilyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        int databaseSizeBeforeUpdate = familyRepository.findAll().size();

        // Update the family
        Family updatedFamily = familyRepository.findById(family.getId()).get();
        // Disconnect from session so that the updates on updatedFamily are not directly saved in db
        em.detach(updatedFamily);
        updatedFamily.name(UPDATED_NAME);

        restFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFamily.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFamily))
            )
            .andExpect(status().isOk());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
        Family testFamily = familyList.get(familyList.size() - 1);
        assertThat(testFamily.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, family.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(family)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilyWithPatch() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        int databaseSizeBeforeUpdate = familyRepository.findAll().size();

        // Update the family using partial update
        Family partialUpdatedFamily = new Family();
        partialUpdatedFamily.setId(family.getId());

        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamily))
            )
            .andExpect(status().isOk());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
        Family testFamily = familyList.get(familyList.size() - 1);
        assertThat(testFamily.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFamilyWithPatch() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        int databaseSizeBeforeUpdate = familyRepository.findAll().size();

        // Update the family using partial update
        Family partialUpdatedFamily = new Family();
        partialUpdatedFamily.setId(family.getId());

        partialUpdatedFamily.name(UPDATED_NAME);

        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamily))
            )
            .andExpect(status().isOk());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
        Family testFamily = familyList.get(familyList.size() - 1);
        assertThat(testFamily.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, family.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();
        family.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(family)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        int databaseSizeBeforeDelete = familyRepository.findAll().size();

        // Delete the family
        restFamilyMockMvc
            .perform(delete(ENTITY_API_URL_ID, family.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
