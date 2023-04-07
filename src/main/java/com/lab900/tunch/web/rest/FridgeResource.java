package com.lab900.tunch.web.rest;

import com.lab900.tunch.domain.Fridge;
import com.lab900.tunch.repository.FridgeRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lab900.tunch.domain.Fridge}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FridgeResource {

    private final Logger log = LoggerFactory.getLogger(FridgeResource.class);

    private static final String ENTITY_NAME = "fridge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FridgeRepository fridgeRepository;

    public FridgeResource(FridgeRepository fridgeRepository) {
        this.fridgeRepository = fridgeRepository;
    }

    /**
     * {@code POST  /fridges} : Create a new fridge.
     *
     * @param fridge the fridge to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fridge, or with status {@code 400 (Bad Request)} if the fridge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fridges")
    public ResponseEntity<Fridge> createFridge(@Valid @RequestBody Fridge fridge) throws URISyntaxException {
        log.debug("REST request to save Fridge : {}", fridge);
        if (fridge.getId() != null) {
            throw new BadRequestAlertException("A new fridge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fridge result = fridgeRepository.save(fridge);
        return ResponseEntity
            .created(new URI("/api/fridges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fridges/:id} : Updates an existing fridge.
     *
     * @param id the id of the fridge to save.
     * @param fridge the fridge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fridge,
     * or with status {@code 400 (Bad Request)} if the fridge is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fridge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fridges/{id}")
    public ResponseEntity<Fridge> updateFridge(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Fridge fridge
    ) throws URISyntaxException {
        log.debug("REST request to update Fridge : {}, {}", id, fridge);
        if (fridge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fridge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fridgeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Fridge result = fridgeRepository.save(fridge);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fridge.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fridges/:id} : Partial updates given fields of an existing fridge, field will ignore if it is null
     *
     * @param id the id of the fridge to save.
     * @param fridge the fridge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fridge,
     * or with status {@code 400 (Bad Request)} if the fridge is not valid,
     * or with status {@code 404 (Not Found)} if the fridge is not found,
     * or with status {@code 500 (Internal Server Error)} if the fridge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fridges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Fridge> partialUpdateFridge(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Fridge fridge
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fridge partially : {}, {}", id, fridge);
        if (fridge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fridge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fridgeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fridge> result = fridgeRepository
            .findById(fridge.getId())
            .map(existingFridge -> {
                if (fridge.getName() != null) {
                    existingFridge.setName(fridge.getName());
                }
                if (fridge.getLocation() != null) {
                    existingFridge.setLocation(fridge.getLocation());
                }

                return existingFridge;
            })
            .map(fridgeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fridge.getId().toString())
        );
    }

    /**
     * {@code GET  /fridges} : get all the fridges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fridges in body.
     */
    @GetMapping("/fridges")
    public List<Fridge> getAllFridges() {
        log.debug("REST request to get all Fridges");
        return fridgeRepository.findAll();
    }

    /**
     * {@code GET  /fridges/:id} : get the "id" fridge.
     *
     * @param id the id of the fridge to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fridge, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fridges/{id}")
    public ResponseEntity<Fridge> getFridge(@PathVariable Long id) {
        log.debug("REST request to get Fridge : {}", id);
        Optional<Fridge> fridge = fridgeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fridge);
    }

    /**
     * {@code DELETE  /fridges/:id} : delete the "id" fridge.
     *
     * @param id the id of the fridge to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fridges/{id}")
    public ResponseEntity<Void> deleteFridge(@PathVariable Long id) {
        log.debug("REST request to delete Fridge : {}", id);
        fridgeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
