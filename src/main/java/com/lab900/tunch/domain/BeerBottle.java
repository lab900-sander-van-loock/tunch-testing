package com.lab900.tunch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A BeerBottle.
 */
@Entity
@Table(name = "beer_bottle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BeerBottle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @ManyToOne
    private Beer beer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "beerBottles" }, allowSetters = true)
    private Fridge fridge;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public BeerBottle id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public BeerBottle expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.expirationDate);
    }

    public Beer getBeer() {
        return this.beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public BeerBottle beer(Beer beer) {
        this.setBeer(beer);
        return this;
    }

    public Fridge getFridge() {
        return this.fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }

    public BeerBottle fridge(Fridge fridge) {
        this.setFridge(fridge);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeerBottle)) {
            return false;
        }
        return id != null && id.equals(((BeerBottle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BeerBottle{" +
            "id=" + getId() +
            ", expirationDate='" + getExpirationDate() + "'" +
            "}";
    }
}
