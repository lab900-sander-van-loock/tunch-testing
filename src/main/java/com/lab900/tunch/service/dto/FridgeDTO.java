package com.lab900.tunch.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.lab900.tunch.domain.Fridge} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FridgeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String location;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FridgeDTO)) {
            return false;
        }

        FridgeDTO fridgeDTO = (FridgeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fridgeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FridgeDTO{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", location='" + getLocation() + "'" +
                "}";
    }
}
