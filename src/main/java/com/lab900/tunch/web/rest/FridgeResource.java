package com.lab900.tunch.web.rest;

import com.lab900.tunch.repository.FridgeRepository;
import com.lab900.tunch.service.FridgeService;
import com.lab900.tunch.service.dto.FridgeDTO;
import com.lab900.tunch.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lab900.tunch.domain.Fridge}.
 */
@RestController
@RequestMapping("/api")
public class FridgeResource {

    private final Logger log = LoggerFactory.getLogger(FridgeResource.class);

    private static final String ENTITY_NAME = "fridge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FridgeService fridgeService;

    private final FridgeRepository fridgeRepository;

    public FridgeResource(FridgeService fridgeService, FridgeRepository fridgeRepository) {
        this.fridgeService = fridgeService;
        this.fridgeRepository = fridgeRepository;
    }

    /**
     * {@code POST  /fridges} : Create a new fridge.
     *
     * @param fridgeDTO the fridgeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fridgeDTO, or with status {@code 400 (Bad Request)} if the fridge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fridges")
    public ResponseEntity<FridgeDTO> createFridge(@Valid @RequestBody FridgeDTO fridgeDTO) throws URISyntaxException {
        log.debug("REST request to save Fridge : {}", fridgeDTO);
        if (fridgeDTO.getId() != null) {
            throw new BadRequestAlertException("A new fridge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FridgeDTO result = fridgeService.save(fridgeDTO);
        return ResponseEntity
            .created(new URI("/api/fridges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fridges/:id} : Updates an existing fridge.
     *
     * @param id        the id of the fridgeDTO to save.
     * @param fridgeDTO the fridgeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fridgeDTO,
     * or with status {@code 400 (Bad Request)} if the fridgeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fridgeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fridges/{id}")
    public ResponseEntity<FridgeDTO> updateFridge(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FridgeDTO fridgeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Fridge : {}, {}", id, fridgeDTO);
        if (fridgeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fridgeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fridgeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FridgeDTO result = fridgeService.update(fridgeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fridgeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fridges/:id} : Partial updates given fields of an existing fridge, field will ignore if it is null
     *
     * @param id        the id of the fridgeDTO to save.
     * @param fridgeDTO the fridgeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fridgeDTO,
     * or with status {@code 400 (Bad Request)} if the fridgeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fridgeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fridgeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fridges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FridgeDTO> partialUpdateFridge(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FridgeDTO fridgeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fridge partially : {}, {}", id, fridgeDTO);
        if (fridgeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fridgeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fridgeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FridgeDTO> result = fridgeService.partialUpdate(fridgeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fridgeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fridges} : get all the fridges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fridges in body.
     */
    @GetMapping("/fridges")
    public List<FridgeDTO> getAllFridges() {
        log.debug("REST request to get all Fridges");
        return fridgeService.findAll();
    }

    /**
     * {@code GET  /fridges/:id} : get the "id" fridge.
     *
     * @param id the id of the fridgeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fridgeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fridges/{id}")
    public ResponseEntity<FridgeDTO> getFridge(@PathVariable Long id) {
        log.debug("REST request to get Fridge : {}", id);
        Optional<FridgeDTO> fridgeDTO = fridgeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fridgeDTO);
    }

    /**
     * {@code DELETE  /fridges/:id} : delete the "id" fridge.
     *
     * @param id the id of the fridgeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fridges/{id}")
    public ResponseEntity<Void> deleteFridge(@PathVariable Long id) {
        log.debug("REST request to delete Fridge : {}", id);
        fridgeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
