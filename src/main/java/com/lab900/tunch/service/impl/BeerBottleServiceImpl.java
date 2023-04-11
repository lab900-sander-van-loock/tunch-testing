package com.lab900.tunch.service.impl;

import com.lab900.tunch.domain.BeerBottle;
import com.lab900.tunch.repository.BeerBottleRepository;
import com.lab900.tunch.service.BeerBottleService;
import com.lab900.tunch.service.dto.BeerBottleDTO;
import com.lab900.tunch.service.mapper.BeerBottleMapper;
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

    private final BeerBottleMapper beerBottleMapper;

    public BeerBottleServiceImpl(BeerBottleRepository beerBottleRepository, BeerBottleMapper beerBottleMapper) {
        this.beerBottleRepository = beerBottleRepository;
        this.beerBottleMapper = beerBottleMapper;
    }

    @Override
    public BeerBottleDTO save(BeerBottleDTO beerBottleDTO) {
        log.debug("Request to save BeerBottle : {}", beerBottleDTO);
        BeerBottle beerBottle = beerBottleMapper.toEntity(beerBottleDTO);
        beerBottle = beerBottleRepository.save(beerBottle);
        return beerBottleMapper.toDto(beerBottle);
    }

    @Override
    public BeerBottleDTO update(BeerBottleDTO beerBottleDTO) {
        log.debug("Request to update BeerBottle : {}", beerBottleDTO);
        BeerBottle beerBottle = beerBottleMapper.toEntity(beerBottleDTO);
        beerBottle = beerBottleRepository.save(beerBottle);
        return beerBottleMapper.toDto(beerBottle);
    }

    @Override
    public Optional<BeerBottleDTO> partialUpdate(BeerBottleDTO beerBottleDTO) {
        log.debug("Request to partially update BeerBottle : {}", beerBottleDTO);

        return beerBottleRepository
            .findById(beerBottleDTO.getId())
            .map(existingBeerBottle -> {
                beerBottleMapper.partialUpdate(existingBeerBottle, beerBottleDTO);

                return existingBeerBottle;
            })
            .map(beerBottleRepository::save)
            .map(beerBottleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeerBottleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BeerBottles");
        return beerBottleRepository.findAll(pageable).map(beerBottleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerBottleDTO> findOne(UUID id) {
        log.debug("Request to get BeerBottle : {}", id);
        return beerBottleRepository.findById(id).map(beerBottleMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete BeerBottle : {}", id);
        beerBottleRepository.deleteById(id);
    }
}
