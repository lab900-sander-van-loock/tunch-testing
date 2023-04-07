package com.lab900.tunch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lab900.tunch.IntegrationTest;
import com.lab900.tunch.domain.Fridge;
import com.lab900.tunch.repository.FridgeRepository;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link FridgeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FridgeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fridges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFridgeMockMvc;

    private Fridge fridge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fridge createEntity(EntityManager em) {
        Fridge fridge = new Fridge().name(DEFAULT_NAME).location(DEFAULT_LOCATION);
        return fridge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fridge createUpdatedEntity(EntityManager em) {
        Fridge fridge = new Fridge().name(UPDATED_NAME).location(UPDATED_LOCATION);
        return fridge;
    }

    @BeforeEach
    public void initTest() {
        fridge = createEntity(em);
    }

    @Test
    @Transactional
    void createFridge() throws Exception {
        int databaseSizeBeforeCreate = fridgeRepository.findAll().size();
        // Create the Fridge
        restFridgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fridge)))
            .andExpect(status().isCreated());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeCreate + 1);
        Fridge testFridge = fridgeList.get(fridgeList.size() - 1);
        assertThat(testFridge.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFridge.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void createFridgeWithExistingId() throws Exception {
        // Create the Fridge with an existing ID
        fridge.setId(1L);

        int databaseSizeBeforeCreate = fridgeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFridgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fridge)))
            .andExpect(status().isBadRequest());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fridgeRepository.findAll().size();
        // set the field null
        fridge.setName(null);

        // Create the Fridge, which fails.

        restFridgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fridge)))
            .andExpect(status().isBadRequest());

        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFridges() throws Exception {
        // Initialize the database
        fridgeRepository.saveAndFlush(fridge);

        // Get all the fridgeList
        restFridgeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fridge.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    @Transactional
    void getFridge() throws Exception {
        // Initialize the database
        fridgeRepository.saveAndFlush(fridge);

        // Get the fridge
        restFridgeMockMvc
            .perform(get(ENTITY_API_URL_ID, fridge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fridge.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getNonExistingFridge() throws Exception {
        // Get the fridge
        restFridgeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFridge() throws Exception {
        // Initialize the database
        fridgeRepository.saveAndFlush(fridge);

        int databaseSizeBeforeUpdate = fridgeRepository.findAll().size();

        // Update the fridge
        Fridge updatedFridge = fridgeRepository.findById(fridge.getId()).get();
        // Disconnect from session so that the updates on updatedFridge are not directly saved in db
        em.detach(updatedFridge);
        updatedFridge.name(UPDATED_NAME).location(UPDATED_LOCATION);

        restFridgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFridge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFridge))
            )
            .andExpect(status().isOk());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeUpdate);
        Fridge testFridge = fridgeList.get(fridgeList.size() - 1);
        assertThat(testFridge.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFridge.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void putNonExistingFridge() throws Exception {
        int databaseSizeBeforeUpdate = fridgeRepository.findAll().size();
        fridge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFridgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fridge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fridge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFridge() throws Exception {
        int databaseSizeBeforeUpdate = fridgeRepository.findAll().size();
        fridge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFridgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fridge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFridge() throws Exception {
        int databaseSizeBeforeUpdate = fridgeRepository.findAll().size();
        fridge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFridgeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fridge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFridgeWithPatch() throws Exception {
        // Initialize the database
        fridgeRepository.saveAndFlush(fridge);

        int databaseSizeBeforeUpdate = fridgeRepository.findAll().size();

        // Update the fridge using partial update
        Fridge partialUpdatedFridge = new Fridge();
        partialUpdatedFridge.setId(fridge.getId());

        partialUpdatedFridge.name(UPDATED_NAME).location(UPDATED_LOCATION);

        restFridgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFridge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFridge))
            )
            .andExpect(status().isOk());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeUpdate);
        Fridge testFridge = fridgeList.get(fridgeList.size() - 1);
        assertThat(testFridge.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFridge.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void fullUpdateFridgeWithPatch() throws Exception {
        // Initialize the database
        fridgeRepository.saveAndFlush(fridge);

        int databaseSizeBeforeUpdate = fridgeRepository.findAll().size();

        // Update the fridge using partial update
        Fridge partialUpdatedFridge = new Fridge();
        partialUpdatedFridge.setId(fridge.getId());

        partialUpdatedFridge.name(UPDATED_NAME).location(UPDATED_LOCATION);

        restFridgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFridge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFridge))
            )
            .andExpect(status().isOk());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeUpdate);
        Fridge testFridge = fridgeList.get(fridgeList.size() - 1);
        assertThat(testFridge.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFridge.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void patchNonExistingFridge() throws Exception {
        int databaseSizeBeforeUpdate = fridgeRepository.findAll().size();
        fridge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFridgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fridge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fridge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFridge() throws Exception {
        int databaseSizeBeforeUpdate = fridgeRepository.findAll().size();
        fridge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFridgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fridge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFridge() throws Exception {
        int databaseSizeBeforeUpdate = fridgeRepository.findAll().size();
        fridge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFridgeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fridge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fridge in the database
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFridge() throws Exception {
        // Initialize the database
        fridgeRepository.saveAndFlush(fridge);

        int databaseSizeBeforeDelete = fridgeRepository.findAll().size();

        // Delete the fridge
        restFridgeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fridge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fridge> fridgeList = fridgeRepository.findAll();
        assertThat(fridgeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
