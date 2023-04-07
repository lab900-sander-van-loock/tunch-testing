package com.lab900.tunch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lab900.tunch.IntegrationTest;
import com.lab900.tunch.domain.BeerBottle;
import com.lab900.tunch.repository.BeerBottleRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
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
 * Integration tests for the {@link BeerBottleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BeerBottleResourceIT {

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/beer-bottles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BeerBottleRepository beerBottleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBeerBottleMockMvc;

    private BeerBottle beerBottle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BeerBottle createEntity(EntityManager em) {
        BeerBottle beerBottle = new BeerBottle().expirationDate(DEFAULT_EXPIRATION_DATE);
        return beerBottle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BeerBottle createUpdatedEntity(EntityManager em) {
        BeerBottle beerBottle = new BeerBottle().expirationDate(UPDATED_EXPIRATION_DATE);
        return beerBottle;
    }

    @BeforeEach
    public void initTest() {
        beerBottle = createEntity(em);
    }

    @Test
    @Transactional
    void createBeerBottle() throws Exception {
        int databaseSizeBeforeCreate = beerBottleRepository.findAll().size();
        // Create the BeerBottle
        restBeerBottleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beerBottle)))
            .andExpect(status().isCreated());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeCreate + 1);
        BeerBottle testBeerBottle = beerBottleList.get(beerBottleList.size() - 1);
        assertThat(testBeerBottle.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void createBeerBottleWithExistingId() throws Exception {
        // Create the BeerBottle with an existing ID
        beerBottleRepository.saveAndFlush(beerBottle);

        int databaseSizeBeforeCreate = beerBottleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeerBottleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beerBottle)))
            .andExpect(status().isBadRequest());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = beerBottleRepository.findAll().size();
        // set the field null
        beerBottle.setExpirationDate(null);

        // Create the BeerBottle, which fails.

        restBeerBottleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beerBottle)))
            .andExpect(status().isBadRequest());

        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBeerBottles() throws Exception {
        // Initialize the database
        beerBottleRepository.saveAndFlush(beerBottle);

        // Get all the beerBottleList
        restBeerBottleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beerBottle.getId().toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getBeerBottle() throws Exception {
        // Initialize the database
        beerBottleRepository.saveAndFlush(beerBottle);

        // Get the beerBottle
        restBeerBottleMockMvc
            .perform(get(ENTITY_API_URL_ID, beerBottle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beerBottle.getId().toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBeerBottle() throws Exception {
        // Get the beerBottle
        restBeerBottleMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBeerBottle() throws Exception {
        // Initialize the database
        beerBottleRepository.saveAndFlush(beerBottle);

        int databaseSizeBeforeUpdate = beerBottleRepository.findAll().size();

        // Update the beerBottle
        BeerBottle updatedBeerBottle = beerBottleRepository.findById(beerBottle.getId()).get();
        // Disconnect from session so that the updates on updatedBeerBottle are not directly saved in db
        em.detach(updatedBeerBottle);
        updatedBeerBottle.expirationDate(UPDATED_EXPIRATION_DATE);

        restBeerBottleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBeerBottle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBeerBottle))
            )
            .andExpect(status().isOk());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeUpdate);
        BeerBottle testBeerBottle = beerBottleList.get(beerBottleList.size() - 1);
        assertThat(testBeerBottle.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingBeerBottle() throws Exception {
        int databaseSizeBeforeUpdate = beerBottleRepository.findAll().size();
        beerBottle.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeerBottleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beerBottle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beerBottle))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBeerBottle() throws Exception {
        int databaseSizeBeforeUpdate = beerBottleRepository.findAll().size();
        beerBottle.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerBottleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beerBottle))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBeerBottle() throws Exception {
        int databaseSizeBeforeUpdate = beerBottleRepository.findAll().size();
        beerBottle.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerBottleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beerBottle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBeerBottleWithPatch() throws Exception {
        // Initialize the database
        beerBottleRepository.saveAndFlush(beerBottle);

        int databaseSizeBeforeUpdate = beerBottleRepository.findAll().size();

        // Update the beerBottle using partial update
        BeerBottle partialUpdatedBeerBottle = new BeerBottle();
        partialUpdatedBeerBottle.setId(beerBottle.getId());

        partialUpdatedBeerBottle.expirationDate(UPDATED_EXPIRATION_DATE);

        restBeerBottleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeerBottle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeerBottle))
            )
            .andExpect(status().isOk());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeUpdate);
        BeerBottle testBeerBottle = beerBottleList.get(beerBottleList.size() - 1);
        assertThat(testBeerBottle.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateBeerBottleWithPatch() throws Exception {
        // Initialize the database
        beerBottleRepository.saveAndFlush(beerBottle);

        int databaseSizeBeforeUpdate = beerBottleRepository.findAll().size();

        // Update the beerBottle using partial update
        BeerBottle partialUpdatedBeerBottle = new BeerBottle();
        partialUpdatedBeerBottle.setId(beerBottle.getId());

        partialUpdatedBeerBottle.expirationDate(UPDATED_EXPIRATION_DATE);

        restBeerBottleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeerBottle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeerBottle))
            )
            .andExpect(status().isOk());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeUpdate);
        BeerBottle testBeerBottle = beerBottleList.get(beerBottleList.size() - 1);
        assertThat(testBeerBottle.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingBeerBottle() throws Exception {
        int databaseSizeBeforeUpdate = beerBottleRepository.findAll().size();
        beerBottle.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeerBottleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, beerBottle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beerBottle))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBeerBottle() throws Exception {
        int databaseSizeBeforeUpdate = beerBottleRepository.findAll().size();
        beerBottle.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerBottleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beerBottle))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBeerBottle() throws Exception {
        int databaseSizeBeforeUpdate = beerBottleRepository.findAll().size();
        beerBottle.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerBottleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(beerBottle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BeerBottle in the database
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBeerBottle() throws Exception {
        // Initialize the database
        beerBottleRepository.saveAndFlush(beerBottle);

        int databaseSizeBeforeDelete = beerBottleRepository.findAll().size();

        // Delete the beerBottle
        restBeerBottleMockMvc
            .perform(delete(ENTITY_API_URL_ID, beerBottle.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BeerBottle> beerBottleList = beerBottleRepository.findAll();
        assertThat(beerBottleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
