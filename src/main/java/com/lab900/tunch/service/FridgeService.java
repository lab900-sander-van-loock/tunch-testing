package com.lab900.tunch.service;

import com.lab900.tunch.domain.Fridge;
import com.lab900.tunch.repository.FridgeRepository;
import com.lab900.tunch.service.dto.FridgeDTO;
import com.lab900.tunch.service.mapper.FridgeMapper;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fridge}.
 */
@Service
@Transactional
public class FridgeService {

    private final Logger log = LoggerFactory.getLogger(FridgeService.class);

    private final FridgeRepository fridgeRepository;

    private final FridgeMapper fridgeMapper;

    public FridgeService(FridgeRepository fridgeRepository, FridgeMapper fridgeMapper) {
        this.fridgeRepository = fridgeRepository;
        this.fridgeMapper = fridgeMapper;
    }

    /**
     * Save a fridge.
     *
     * @param fridgeDTO the entity to save.
     * @return the persisted entity.
     */
    public FridgeDTO save(FridgeDTO fridgeDTO) {
        log.debug("Request to save Fridge : {}", fridgeDTO);
        Fridge fridge = fridgeMapper.toEntity(fridgeDTO);
        fridge = fridgeRepository.save(fridge);
        return fridgeMapper.toDto(fridge);
    }

    /**
     * Update a fridge.
     *
     * @param fridgeDTO the entity to save.
     * @return the persisted entity.
     */
    public FridgeDTO update(FridgeDTO fridgeDTO) {
        log.debug("Request to update Fridge : {}", fridgeDTO);
        Fridge fridge = fridgeMapper.toEntity(fridgeDTO);
        fridge = fridgeRepository.save(fridge);
        return fridgeMapper.toDto(fridge);
    }

    /**
     * Partially update a fridge.
     *
     * @param fridgeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FridgeDTO> partialUpdate(FridgeDTO fridgeDTO) {
        log.debug("Request to partially update Fridge : {}", fridgeDTO);

        return fridgeRepository
            .findById(fridgeDTO.getId())
            .map(existingFridge -> {
                fridgeMapper.partialUpdate(existingFridge, fridgeDTO);

                return existingFridge;
            })
            .map(fridgeRepository::save)
            .map(fridgeMapper::toDto);
    }

    /**
     * Get all the fridges.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FridgeDTO> findAll() {
        log.debug("Request to get all Fridges");
        return fridgeRepository.findAll().stream().map(fridgeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one fridge by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FridgeDTO> findOne(Long id) {
        log.debug("Request to get Fridge : {}", id);
        return fridgeRepository.findById(id).map(fridgeMapper::toDto);
    }

    /**
     * Delete the fridge by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Fridge : {}", id);
        fridgeRepository.deleteById(id);
    }

    public Duration howTongToCoolUntil(BigDecimal targetTemperatureDegreesCelsius) {
        if (BigDecimal.ZERO.compareTo(targetTemperatureDegreesCelsius) < 0) {
            throw new IllegalArgumentException("Target temperature must be positive");
        }
        return Duration.ofSeconds((long) (Math.random() * 200));
    }
}
