package com.lab900.tunch.web.rest;

import com.lab900.tunch.domain.BeerBottle;
import com.lab900.tunch.repository.BeerBottleRepository;
import com.lab900.tunch.service.BeerBottleService;
import com.lab900.tunch.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lab900.tunch.domain.BeerBottle}.
 */
@RestController
@RequestMapping("/api")
public class BeerBottleResource {

    private final Logger log = LoggerFactory.getLogger(BeerBottleResource.class);

    private static final String ENTITY_NAME = "beerBottle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeerBottleService beerBottleService;

    private final BeerBottleRepository beerBottleRepository;

    public BeerBottleResource(BeerBottleService beerBottleService, BeerBottleRepository beerBottleRepository) {
        this.beerBottleService = beerBottleService;
        this.beerBottleRepository = beerBottleRepository;
    }

    /**
     * {@code POST  /beer-bottles} : Create a new beerBottle.
     *
     * @param beerBottle the beerBottle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beerBottle, or with status {@code 400 (Bad Request)} if the beerBottle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beer-bottles")
    public ResponseEntity<BeerBottle> createBeerBottle(@Valid @RequestBody BeerBottle beerBottle) throws URISyntaxException {
        log.debug("REST request to save BeerBottle : {}", beerBottle);
        if (beerBottle.getId() != null) {
            throw new BadRequestAlertException("A new beerBottle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BeerBottle result = beerBottleService.save(beerBottle);
        return ResponseEntity
            .created(new URI("/api/beer-bottles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beer-bottles/:id} : Updates an existing beerBottle.
     *
     * @param id the id of the beerBottle to save.
     * @param beerBottle the beerBottle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerBottle,
     * or with status {@code 400 (Bad Request)} if the beerBottle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beerBottle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beer-bottles/{id}")
    public ResponseEntity<BeerBottle> updateBeerBottle(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody BeerBottle beerBottle
    ) throws URISyntaxException {
        log.debug("REST request to update BeerBottle : {}, {}", id, beerBottle);
        if (beerBottle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beerBottle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beerBottleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BeerBottle result = beerBottleService.update(beerBottle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beerBottle.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /beer-bottles/:id} : Partial updates given fields of an existing beerBottle, field will ignore if it is null
     *
     * @param id the id of the beerBottle to save.
     * @param beerBottle the beerBottle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerBottle,
     * or with status {@code 400 (Bad Request)} if the beerBottle is not valid,
     * or with status {@code 404 (Not Found)} if the beerBottle is not found,
     * or with status {@code 500 (Internal Server Error)} if the beerBottle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/beer-bottles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BeerBottle> partialUpdateBeerBottle(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody BeerBottle beerBottle
    ) throws URISyntaxException {
        log.debug("REST request to partial update BeerBottle partially : {}, {}", id, beerBottle);
        if (beerBottle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beerBottle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beerBottleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BeerBottle> result = beerBottleService.partialUpdate(beerBottle);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beerBottle.getId().toString())
        );
    }

    /**
     * {@code GET  /beer-bottles} : get all the beerBottles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beerBottles in body.
     */
    @GetMapping("/beer-bottles")
    public ResponseEntity<List<BeerBottle>> getAllBeerBottles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of BeerBottles");
        Page<BeerBottle> page = beerBottleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beer-bottles/:id} : get the "id" beerBottle.
     *
     * @param id the id of the beerBottle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beerBottle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beer-bottles/{id}")
    public ResponseEntity<BeerBottle> getBeerBottle(@PathVariable UUID id) {
        log.debug("REST request to get BeerBottle : {}", id);
        Optional<BeerBottle> beerBottle = beerBottleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beerBottle);
    }

    /**
     * {@code DELETE  /beer-bottles/:id} : delete the "id" beerBottle.
     *
     * @param id the id of the beerBottle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beer-bottles/{id}")
    public ResponseEntity<Void> deleteBeerBottle(@PathVariable UUID id) {
        log.debug("REST request to delete BeerBottle : {}", id);
        beerBottleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
