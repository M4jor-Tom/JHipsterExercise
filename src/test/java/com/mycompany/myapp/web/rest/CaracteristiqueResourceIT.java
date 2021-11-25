package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Caracteristique;
import com.mycompany.myapp.repository.CaracteristiqueRepository;
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
 * Integration tests for the {@link CaracteristiqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaracteristiqueResourceIT {

    private static final Integer DEFAULT_I_D_CARACTERISTIQUE = 1;
    private static final Integer UPDATED_I_D_CARACTERISTIQUE = 2;

    private static final String DEFAULT_MARQUE = "AAAAAAAAAA";
    private static final String UPDATED_MARQUE = "BBBBBBBBBB";

    private static final String DEFAULT_MODELE = "AAAAAAAAAA";
    private static final String UPDATED_MODELE = "BBBBBBBBBB";

    private static final String DEFAULT_TAILLE = "AAAAAAAAAA";
    private static final String UPDATED_TAILLE = "BBBBBBBBBB";

    private static final String DEFAULT_COULEUR = "AAAAAAAAAA";
    private static final String UPDATED_COULEUR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/caracteristiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CaracteristiqueRepository caracteristiqueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaracteristiqueMockMvc;

    private Caracteristique caracteristique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caracteristique createEntity(EntityManager em) {
        Caracteristique caracteristique = new Caracteristique()
            .iDCaracteristique(DEFAULT_I_D_CARACTERISTIQUE)
            .marque(DEFAULT_MARQUE)
            .modele(DEFAULT_MODELE)
            .taille(DEFAULT_TAILLE)
            .couleur(DEFAULT_COULEUR);
        return caracteristique;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caracteristique createUpdatedEntity(EntityManager em) {
        Caracteristique caracteristique = new Caracteristique()
            .iDCaracteristique(UPDATED_I_D_CARACTERISTIQUE)
            .marque(UPDATED_MARQUE)
            .modele(UPDATED_MODELE)
            .taille(UPDATED_TAILLE)
            .couleur(UPDATED_COULEUR);
        return caracteristique;
    }

    @BeforeEach
    public void initTest() {
        caracteristique = createEntity(em);
    }

    @Test
    @Transactional
    void createCaracteristique() throws Exception {
        int databaseSizeBeforeCreate = caracteristiqueRepository.findAll().size();
        // Create the Caracteristique
        restCaracteristiqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristique))
            )
            .andExpect(status().isCreated());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeCreate + 1);
        Caracteristique testCaracteristique = caracteristiqueList.get(caracteristiqueList.size() - 1);
        assertThat(testCaracteristique.getiDCaracteristique()).isEqualTo(DEFAULT_I_D_CARACTERISTIQUE);
        assertThat(testCaracteristique.getMarque()).isEqualTo(DEFAULT_MARQUE);
        assertThat(testCaracteristique.getModele()).isEqualTo(DEFAULT_MODELE);
        assertThat(testCaracteristique.getTaille()).isEqualTo(DEFAULT_TAILLE);
        assertThat(testCaracteristique.getCouleur()).isEqualTo(DEFAULT_COULEUR);
    }

    @Test
    @Transactional
    void createCaracteristiqueWithExistingId() throws Exception {
        // Create the Caracteristique with an existing ID
        caracteristique.setId(1L);

        int databaseSizeBeforeCreate = caracteristiqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaracteristiqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristique))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCaracteristiques() throws Exception {
        // Initialize the database
        caracteristiqueRepository.saveAndFlush(caracteristique);

        // Get all the caracteristiqueList
        restCaracteristiqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caracteristique.getId().intValue())))
            .andExpect(jsonPath("$.[*].iDCaracteristique").value(hasItem(DEFAULT_I_D_CARACTERISTIQUE)))
            .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE)))
            .andExpect(jsonPath("$.[*].modele").value(hasItem(DEFAULT_MODELE)))
            .andExpect(jsonPath("$.[*].taille").value(hasItem(DEFAULT_TAILLE)))
            .andExpect(jsonPath("$.[*].couleur").value(hasItem(DEFAULT_COULEUR)));
    }

    @Test
    @Transactional
    void getCaracteristique() throws Exception {
        // Initialize the database
        caracteristiqueRepository.saveAndFlush(caracteristique);

        // Get the caracteristique
        restCaracteristiqueMockMvc
            .perform(get(ENTITY_API_URL_ID, caracteristique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caracteristique.getId().intValue()))
            .andExpect(jsonPath("$.iDCaracteristique").value(DEFAULT_I_D_CARACTERISTIQUE))
            .andExpect(jsonPath("$.marque").value(DEFAULT_MARQUE))
            .andExpect(jsonPath("$.modele").value(DEFAULT_MODELE))
            .andExpect(jsonPath("$.taille").value(DEFAULT_TAILLE))
            .andExpect(jsonPath("$.couleur").value(DEFAULT_COULEUR));
    }

    @Test
    @Transactional
    void getNonExistingCaracteristique() throws Exception {
        // Get the caracteristique
        restCaracteristiqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCaracteristique() throws Exception {
        // Initialize the database
        caracteristiqueRepository.saveAndFlush(caracteristique);

        int databaseSizeBeforeUpdate = caracteristiqueRepository.findAll().size();

        // Update the caracteristique
        Caracteristique updatedCaracteristique = caracteristiqueRepository.findById(caracteristique.getId()).get();
        // Disconnect from session so that the updates on updatedCaracteristique are not directly saved in db
        em.detach(updatedCaracteristique);
        updatedCaracteristique
            .iDCaracteristique(UPDATED_I_D_CARACTERISTIQUE)
            .marque(UPDATED_MARQUE)
            .modele(UPDATED_MODELE)
            .taille(UPDATED_TAILLE)
            .couleur(UPDATED_COULEUR);

        restCaracteristiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCaracteristique.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCaracteristique))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeUpdate);
        Caracteristique testCaracteristique = caracteristiqueList.get(caracteristiqueList.size() - 1);
        assertThat(testCaracteristique.getiDCaracteristique()).isEqualTo(UPDATED_I_D_CARACTERISTIQUE);
        assertThat(testCaracteristique.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testCaracteristique.getModele()).isEqualTo(UPDATED_MODELE);
        assertThat(testCaracteristique.getTaille()).isEqualTo(UPDATED_TAILLE);
        assertThat(testCaracteristique.getCouleur()).isEqualTo(UPDATED_COULEUR);
    }

    @Test
    @Transactional
    void putNonExistingCaracteristique() throws Exception {
        int databaseSizeBeforeUpdate = caracteristiqueRepository.findAll().size();
        caracteristique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaracteristiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caracteristique.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caracteristique))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaracteristique() throws Exception {
        int databaseSizeBeforeUpdate = caracteristiqueRepository.findAll().size();
        caracteristique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caracteristique))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaracteristique() throws Exception {
        int databaseSizeBeforeUpdate = caracteristiqueRepository.findAll().size();
        caracteristique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristiqueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristique))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaracteristiqueWithPatch() throws Exception {
        // Initialize the database
        caracteristiqueRepository.saveAndFlush(caracteristique);

        int databaseSizeBeforeUpdate = caracteristiqueRepository.findAll().size();

        // Update the caracteristique using partial update
        Caracteristique partialUpdatedCaracteristique = new Caracteristique();
        partialUpdatedCaracteristique.setId(caracteristique.getId());

        restCaracteristiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaracteristique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaracteristique))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeUpdate);
        Caracteristique testCaracteristique = caracteristiqueList.get(caracteristiqueList.size() - 1);
        assertThat(testCaracteristique.getiDCaracteristique()).isEqualTo(DEFAULT_I_D_CARACTERISTIQUE);
        assertThat(testCaracteristique.getMarque()).isEqualTo(DEFAULT_MARQUE);
        assertThat(testCaracteristique.getModele()).isEqualTo(DEFAULT_MODELE);
        assertThat(testCaracteristique.getTaille()).isEqualTo(DEFAULT_TAILLE);
        assertThat(testCaracteristique.getCouleur()).isEqualTo(DEFAULT_COULEUR);
    }

    @Test
    @Transactional
    void fullUpdateCaracteristiqueWithPatch() throws Exception {
        // Initialize the database
        caracteristiqueRepository.saveAndFlush(caracteristique);

        int databaseSizeBeforeUpdate = caracteristiqueRepository.findAll().size();

        // Update the caracteristique using partial update
        Caracteristique partialUpdatedCaracteristique = new Caracteristique();
        partialUpdatedCaracteristique.setId(caracteristique.getId());

        partialUpdatedCaracteristique
            .iDCaracteristique(UPDATED_I_D_CARACTERISTIQUE)
            .marque(UPDATED_MARQUE)
            .modele(UPDATED_MODELE)
            .taille(UPDATED_TAILLE)
            .couleur(UPDATED_COULEUR);

        restCaracteristiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaracteristique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaracteristique))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeUpdate);
        Caracteristique testCaracteristique = caracteristiqueList.get(caracteristiqueList.size() - 1);
        assertThat(testCaracteristique.getiDCaracteristique()).isEqualTo(UPDATED_I_D_CARACTERISTIQUE);
        assertThat(testCaracteristique.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testCaracteristique.getModele()).isEqualTo(UPDATED_MODELE);
        assertThat(testCaracteristique.getTaille()).isEqualTo(UPDATED_TAILLE);
        assertThat(testCaracteristique.getCouleur()).isEqualTo(UPDATED_COULEUR);
    }

    @Test
    @Transactional
    void patchNonExistingCaracteristique() throws Exception {
        int databaseSizeBeforeUpdate = caracteristiqueRepository.findAll().size();
        caracteristique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaracteristiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caracteristique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caracteristique))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaracteristique() throws Exception {
        int databaseSizeBeforeUpdate = caracteristiqueRepository.findAll().size();
        caracteristique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caracteristique))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaracteristique() throws Exception {
        int databaseSizeBeforeUpdate = caracteristiqueRepository.findAll().size();
        caracteristique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristiqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caracteristique))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaracteristique() throws Exception {
        // Initialize the database
        caracteristiqueRepository.saveAndFlush(caracteristique);

        int databaseSizeBeforeDelete = caracteristiqueRepository.findAll().size();

        // Delete the caracteristique
        restCaracteristiqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, caracteristique.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caracteristique> caracteristiqueList = caracteristiqueRepository.findAll();
        assertThat(caracteristiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
