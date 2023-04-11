package com.lab900.tunch.service;

import com.lab900.tunch.service.dto.BeerBottleDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.lab900.tunch.domain.BeerBottle}.
 */
public interface BeerBottleService {
    /**
     * Save a beerBottle.
     *
     * @param beerBottleDTO the entity to save.
     * @return the persisted entity.
     */
    BeerBottleDTO save(BeerBottleDTO beerBottleDTO);

    /**
     * Updates a beerBottle.
     *
     * @param beerBottleDTO the entity to update.
     * @return the persisted entity.
     */
    BeerBottleDTO update(BeerBottleDTO beerBottleDTO);

    /**
     * Partially updates a beerBottle.
     *
     * @param beerBottleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BeerBottleDTO> partialUpdate(BeerBottleDTO beerBottleDTO);

    /**
     * Get all the beerBottles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BeerBottleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" beerBottle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BeerBottleDTO> findOne(UUID id);

    /**
     * Delete the "id" beerBottle.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
