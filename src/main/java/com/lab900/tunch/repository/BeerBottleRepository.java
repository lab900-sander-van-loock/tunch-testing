package com.lab900.tunch.repository;

import com.lab900.tunch.domain.BeerBottle;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BeerBottle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeerBottleRepository extends JpaRepository<BeerBottle, UUID> {}
