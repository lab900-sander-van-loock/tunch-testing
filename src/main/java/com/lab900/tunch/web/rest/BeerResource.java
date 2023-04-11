package com.lab900.tunch.web.rest;

import com.lab900.tunch.repository.BeerRepository;
import com.lab900.tunch.service.BeerService;
import com.lab900.tunch.service.dto.BeerDTO;
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
 * REST controller for managing {@link com.lab900.tunch.domain.Beer}.
 */
@RestController
@RequestMapping("/api")
public class BeerResource {

    private final Logger log = LoggerFactory.getLogger(BeerResource.class);

    private static final String ENTITY_NAME = "beer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeerService beerService;

    private final BeerRepository beerRepository;

    public BeerResource(BeerService beerService, BeerRepository beerRepository) {
        this.beerService = beerService;
        this.beerRepository = beerRepository;
    }

    /**
     * {@code POST  /beers} : Create a new beer.
     *
     * @param beerDTO the beerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beerDTO, or with status {@code 400 (Bad Request)} if the beer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beers")
    public ResponseEntity<BeerDTO> createBeer(@Valid @RequestBody BeerDTO beerDTO) throws URISyntaxException {
        log.debug("REST request to save Beer : {}", beerDTO);
        if (beerDTO.getId() != null) {
            throw new BadRequestAlertException("A new beer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BeerDTO result = beerService.save(beerDTO);
        return ResponseEntity
            .created(new URI("/api/beers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beers/:id} : Updates an existing beer.
     *
     * @param id      the id of the beerDTO to save.
     * @param beerDTO the beerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerDTO,
     * or with status {@code 400 (Bad Request)} if the beerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beers/{id}")
    public ResponseEntity<BeerDTO> updateBeer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BeerDTO beerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Beer : {}, {}", id, beerDTO);
        if (beerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BeerDTO result = beerService.update(beerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /beers/:id} : Partial updates given fields of an existing beer, field will ignore if it is null
     *
     * @param id      the id of the beerDTO to save.
     * @param beerDTO the beerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerDTO,
     * or with status {@code 400 (Bad Request)} if the beerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the beerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the beerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/beers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BeerDTO> partialUpdateBeer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BeerDTO beerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Beer partially : {}, {}", id, beerDTO);
        if (beerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BeerDTO> result = beerService.partialUpdate(beerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /beers} : get all the beers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beers in body.
     */
    @GetMapping("/beers")
    public List<BeerDTO> getAllBeers() {
        log.debug("REST request to get all Beers");
        return beerService.findAll();
    }

    /**
     * {@code GET  /beers/:id} : get the "id" beer.
     *
     * @param id the id of the beerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beers/{id}")
    public ResponseEntity<BeerDTO> getBeer(@PathVariable Long id) {
        log.debug("REST request to get Beer : {}", id);
        Optional<BeerDTO> beerDTO = beerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beerDTO);
    }

    /**
     * {@code DELETE  /beers/:id} : delete the "id" beer.
     *
     * @param id the id of the beerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beers/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable Long id) {
        log.debug("REST request to delete Beer : {}", id);
        beerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
