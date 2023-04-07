package com.lab900.tunch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Fridge.
 */
@Entity
@Table(name = "fridge")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fridge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "fridge")
    @JsonIgnoreProperties(value = { "beer", "fridge" }, allowSetters = true)
    private Set<BeerBottle> beerBottles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fridge id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Fridge name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public Fridge location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<BeerBottle> getBeerBottles() {
        return this.beerBottles;
    }

    public void setBeerBottles(Set<BeerBottle> beerBottles) {
        if (this.beerBottles != null) {
            this.beerBottles.forEach(i -> i.setFridge(null));
        }
        if (beerBottles != null) {
            beerBottles.forEach(i -> i.setFridge(this));
        }
        this.beerBottles = beerBottles;
    }

    public Fridge beerBottles(Set<BeerBottle> beerBottles) {
        this.setBeerBottles(beerBottles);
        return this;
    }

    public Fridge addBeerBottle(BeerBottle beerBottle) {
        this.beerBottles.add(beerBottle);
        beerBottle.setFridge(this);
        return this;
    }

    public Fridge removeBeerBottle(BeerBottle beerBottle) {
        this.beerBottles.remove(beerBottle);
        beerBottle.setFridge(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fridge)) {
            return false;
        }
        return id != null && id.equals(((Fridge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fridge{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
