package com.lab900.tunch.service.impl;

import com.lab900.tunch.domain.BeerBottle;
import com.lab900.tunch.repository.BeerBottleRepository;
import com.lab900.tunch.service.BeerBottleService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BeerBottle}.
 */
@Service
@Transactional
public class BeerBottleServiceImpl implements BeerBottleService {

    private final Logger log = LoggerFactory.getLogger(BeerBottleServiceImpl.class);

    private final BeerBottleRepository beerBottleRepository;

    public BeerBottleServiceImpl(BeerBottleRepository beerBottleRepository) {
        this.beerBottleRepository = beerBottleRepository;
    }

    @Override
    public BeerBottle save(BeerBottle beerBottle) {
        log.debug("Request to save BeerBottle : {}", beerBottle);
        return beerBottleRepository.save(beerBottle);
    }

    @Override
    public BeerBottle update(BeerBottle beerBottle) {
        log.debug("Request to update BeerBottle : {}", beerBottle);
        return beerBottleRepository.save(beerBottle);
    }

    @Override
    public Optional<BeerBottle> partialUpdate(BeerBottle beerBottle) {
        log.debug("Request to partially update BeerBottle : {}", beerBottle);

        return beerBottleRepository
            .findById(beerBottle.getId())
            .map(existingBeerBottle -> {
                if (beerBottle.getExpirationDate() != null) {
                    existingBeerBottle.setExpirationDate(beerBottle.getExpirationDate());
                }

                return existingBeerBottle;
            })
            .map(beerBottleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeerBottle> findAll(Pageable pageable) {
        log.debug("Request to get all BeerBottles");
        return beerBottleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerBottle> findOne(UUID id) {
        log.debug("Request to get BeerBottle : {}", id);
        return beerBottleRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete BeerBottle : {}", id);
        beerBottleRepository.deleteById(id);
    }
}
