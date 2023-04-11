package com.lab900.tunch.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.lab900.tunch.domain.Beer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BeerDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String brewery;

    @NotNull
    private Double percentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrewery() {
        return brewery;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeerDTO)) {
            return false;
        }

        BeerDTO beerDTO = (BeerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, beerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BeerDTO{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", brewery='" + getBrewery() + "'" +
                ", percentage=" + getPercentage() +
                "}";
    }
}
