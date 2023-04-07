package com.lab900.tunch.repository;

import com.lab900.tunch.domain.Fridge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Fridge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FridgeRepository extends JpaRepository<Fridge, Long> {}
