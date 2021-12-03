package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Family;
import com.mycompany.myapp.domain.SubFamily;
import com.mycompany.myapp.repository.SubFamilyRepository;
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
 * Integration tests for the {@link SubFamilyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubFamilyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sub-families";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubFamilyRepository subFamilyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubFamilyMockMvc;

    private SubFamily subFamily;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubFamily createEntity(EntityManager em) {
        SubFamily subFamily = new SubFamily().name(DEFAULT_NAME);
        // Add required entity
        Family family;
        if (TestUtil.findAll(em, Family.class).isEmpty()) {
            family = FamilyResourceIT.createEntity(em);
            em.persist(family);
            em.flush();
        } else {
            family = TestUtil.findAll(em, Family.class).get(0);
        }
        subFamily.setFamily(family);
        return subFamily;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubFamily createUpdatedEntity(EntityManager em) {
        SubFamily subFamily = new SubFamily().name(UPDATED_NAME);
        // Add required entity
        Family family;
        if (TestUtil.findAll(em, Family.class).isEmpty()) {
            family = FamilyResourceIT.createUpdatedEntity(em);
            em.persist(family);
            em.flush();
        } else {
            family = TestUtil.findAll(em, Family.class).get(0);
        }
        subFamily.setFamily(family);
        return subFamily;
    }

    @BeforeEach
    public void initTest() {
        subFamily = createEntity(em);
    }

    @Test
    @Transactional
    void createSubFamily() throws Exception {
        int databaseSizeBeforeCreate = subFamilyRepository.findAll().size();
        // Create the SubFamily
        restSubFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subFamily)))
            .andExpect(status().isCreated());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeCreate + 1);
        SubFamily testSubFamily = subFamilyList.get(subFamilyList.size() - 1);
        assertThat(testSubFamily.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSubFamilyWithExistingId() throws Exception {
        // Create the SubFamily with an existing ID
        subFamily.setId(1L);

        int databaseSizeBeforeCreate = subFamilyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subFamily)))
            .andExpect(status().isBadRequest());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subFamilyRepository.findAll().size();
        // set the field null
        subFamily.setName(null);

        // Create the SubFamily, which fails.

        restSubFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subFamily)))
            .andExpect(status().isBadRequest());

        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubFamilies() throws Exception {
        // Initialize the database
        subFamilyRepository.saveAndFlush(subFamily);

        // Get all the subFamilyList
        restSubFamilyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subFamily.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSubFamily() throws Exception {
        // Initialize the database
        subFamilyRepository.saveAndFlush(subFamily);

        // Get the subFamily
        restSubFamilyMockMvc
            .perform(get(ENTITY_API_URL_ID, subFamily.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subFamily.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSubFamily() throws Exception {
        // Get the subFamily
        restSubFamilyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubFamily() throws Exception {
        // Initialize the database
        subFamilyRepository.saveAndFlush(subFamily);

        int databaseSizeBeforeUpdate = subFamilyRepository.findAll().size();

        // Update the subFamily
        SubFamily updatedSubFamily = subFamilyRepository.findById(subFamily.getId()).get();
        // Disconnect from session so that the updates on updatedSubFamily are not directly saved in db
        em.detach(updatedSubFamily);
        updatedSubFamily.name(UPDATED_NAME);

        restSubFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubFamily.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubFamily))
            )
            .andExpect(status().isOk());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeUpdate);
        SubFamily testSubFamily = subFamilyList.get(subFamilyList.size() - 1);
        assertThat(testSubFamily.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSubFamily() throws Exception {
        int databaseSizeBeforeUpdate = subFamilyRepository.findAll().size();
        subFamily.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subFamily.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subFamily))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubFamily() throws Exception {
        int databaseSizeBeforeUpdate = subFamilyRepository.findAll().size();
        subFamily.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subFamily))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubFamily() throws Exception {
        int databaseSizeBeforeUpdate = subFamilyRepository.findAll().size();
        subFamily.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubFamilyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subFamily)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubFamilyWithPatch() throws Exception {
        // Initialize the database
        subFamilyRepository.saveAndFlush(subFamily);

        int databaseSizeBeforeUpdate = subFamilyRepository.findAll().size();

        // Update the subFamily using partial update
        SubFamily partialUpdatedSubFamily = new SubFamily();
        partialUpdatedSubFamily.setId(subFamily.getId());

        partialUpdatedSubFamily.name(UPDATED_NAME);

        restSubFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubFamily))
            )
            .andExpect(status().isOk());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeUpdate);
        SubFamily testSubFamily = subFamilyList.get(subFamilyList.size() - 1);
        assertThat(testSubFamily.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSubFamilyWithPatch() throws Exception {
        // Initialize the database
        subFamilyRepository.saveAndFlush(subFamily);

        int databaseSizeBeforeUpdate = subFamilyRepository.findAll().size();

        // Update the subFamily using partial update
        SubFamily partialUpdatedSubFamily = new SubFamily();
        partialUpdatedSubFamily.setId(subFamily.getId());

        partialUpdatedSubFamily.name(UPDATED_NAME);

        restSubFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubFamily))
            )
            .andExpect(status().isOk());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeUpdate);
        SubFamily testSubFamily = subFamilyList.get(subFamilyList.size() - 1);
        assertThat(testSubFamily.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSubFamily() throws Exception {
        int databaseSizeBeforeUpdate = subFamilyRepository.findAll().size();
        subFamily.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subFamily))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubFamily() throws Exception {
        int databaseSizeBeforeUpdate = subFamilyRepository.findAll().size();
        subFamily.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subFamily))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubFamily() throws Exception {
        int databaseSizeBeforeUpdate = subFamilyRepository.findAll().size();
        subFamily.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subFamily))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubFamily in the database
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubFamily() throws Exception {
        // Initialize the database
        subFamilyRepository.saveAndFlush(subFamily);

        int databaseSizeBeforeDelete = subFamilyRepository.findAll().size();

        // Delete the subFamily
        restSubFamilyMockMvc
            .perform(delete(ENTITY_API_URL_ID, subFamily.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubFamily> subFamilyList = subFamilyRepository.findAll();
        assertThat(subFamilyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
