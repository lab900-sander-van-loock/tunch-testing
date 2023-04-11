package com.lab900.tunch.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.lab900.tunch.domain.BeerBottle} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BeerBottleDTO implements Serializable {

    private UUID id;

    @NotNull
    private LocalDate expirationDate;

    private BeerDTO beer;

    private FridgeDTO fridge;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BeerDTO getBeer() {
        return beer;
    }

    public void setBeer(BeerDTO beer) {
        this.beer = beer;
    }

    public FridgeDTO getFridge() {
        return fridge;
    }

    public void setFridge(FridgeDTO fridge) {
        this.fridge = fridge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeerBottleDTO)) {
            return false;
        }

        BeerBottleDTO beerBottleDTO = (BeerBottleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, beerBottleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BeerBottleDTO{" +
                "id='" + getId() + "'" +
                ", expirationDate='" + getExpirationDate() + "'" +
                ", beer=" + getBeer() +
                ", fridge=" + getFridge() +
                "}";
    }
}
