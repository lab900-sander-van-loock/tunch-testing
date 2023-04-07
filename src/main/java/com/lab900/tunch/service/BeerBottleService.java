package com.lab900.tunch.service;

import com.lab900.tunch.domain.BeerBottle;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link BeerBottle}.
 */
public interface BeerBottleService {
    /**
     * Save a beerBottle.
     *
     * @param beerBottle the entity to save.
     * @return the persisted entity.
     */
    BeerBottle save(BeerBottle beerBottle);

    /**
     * Updates a beerBottle.
     *
     * @param beerBottle the entity to update.
     * @return the persisted entity.
     */
    BeerBottle update(BeerBottle beerBottle);

    /**
     * Partially updates a beerBottle.
     *
     * @param beerBottle the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BeerBottle> partialUpdate(BeerBottle beerBottle);

    /**
     * Get all the beerBottles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BeerBottle> findAll(Pageable pageable);

    /**
     * Get the "id" beerBottle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BeerBottle> findOne(UUID id);

    /**
     * Delete the "id" beerBottle.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
