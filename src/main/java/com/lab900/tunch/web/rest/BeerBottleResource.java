package com.lab900.tunch.web.rest;

import com.lab900.tunch.repository.BeerBottleRepository;
import com.lab900.tunch.service.BeerBottleService;
import com.lab900.tunch.service.dto.BeerBottleDTO;
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
     * @param beerBottleDTO the beerBottleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beerBottleDTO, or with status {@code 400 (Bad Request)} if the beerBottle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beer-bottles")
    public ResponseEntity<BeerBottleDTO> createBeerBottle(@Valid @RequestBody BeerBottleDTO beerBottleDTO) throws URISyntaxException {
        log.debug("REST request to save BeerBottle : {}", beerBottleDTO);
        if (beerBottleDTO.getId() != null) {
            throw new BadRequestAlertException("A new beerBottle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BeerBottleDTO result = beerBottleService.save(beerBottleDTO);
        return ResponseEntity
            .created(new URI("/api/beer-bottles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beer-bottles/:id} : Updates an existing beerBottle.
     *
     * @param id            the id of the beerBottleDTO to save.
     * @param beerBottleDTO the beerBottleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerBottleDTO,
     * or with status {@code 400 (Bad Request)} if the beerBottleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beerBottleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beer-bottles/{id}")
    public ResponseEntity<BeerBottleDTO> updateBeerBottle(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody BeerBottleDTO beerBottleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BeerBottle : {}, {}", id, beerBottleDTO);
        if (beerBottleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beerBottleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beerBottleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BeerBottleDTO result = beerBottleService.update(beerBottleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beerBottleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /beer-bottles/:id} : Partial updates given fields of an existing beerBottle, field will ignore if it is null
     *
     * @param id            the id of the beerBottleDTO to save.
     * @param beerBottleDTO the beerBottleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerBottleDTO,
     * or with status {@code 400 (Bad Request)} if the beerBottleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the beerBottleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the beerBottleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/beer-bottles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BeerBottleDTO> partialUpdateBeerBottle(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody BeerBottleDTO beerBottleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BeerBottle partially : {}, {}", id, beerBottleDTO);
        if (beerBottleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beerBottleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beerBottleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BeerBottleDTO> result = beerBottleService.partialUpdate(beerBottleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beerBottleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /beer-bottles} : get all the beerBottles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beerBottles in body.
     */
    @GetMapping("/beer-bottles")
    public ResponseEntity<List<BeerBottleDTO>> getAllBeerBottles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of BeerBottles");
        Page<BeerBottleDTO> page = beerBottleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beer-bottles/:id} : get the "id" beerBottle.
     *
     * @param id the id of the beerBottleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beerBottleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beer-bottles/{id}")
    public ResponseEntity<BeerBottleDTO> getBeerBottle(@PathVariable UUID id) {
        log.debug("REST request to get BeerBottle : {}", id);
        Optional<BeerBottleDTO> beerBottleDTO = beerBottleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beerBottleDTO);
    }

    /**
     * {@code DELETE  /beer-bottles/:id} : delete the "id" beerBottle.
     *
     * @param id the id of the beerBottleDTO to delete.
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
