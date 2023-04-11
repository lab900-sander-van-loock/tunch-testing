package com.lab900.tunch.service.mapper;

import com.lab900.tunch.domain.Beer;
import com.lab900.tunch.domain.BeerBottle;
import com.lab900.tunch.domain.Fridge;
import com.lab900.tunch.service.dto.BeerBottleDTO;
import com.lab900.tunch.service.dto.BeerDTO;
import com.lab900.tunch.service.dto.FridgeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link BeerBottle} and its DTO {@link BeerBottleDTO}.
 */
@Mapper(componentModel = "spring")
public interface BeerBottleMapper extends EntityMapper<BeerBottleDTO, BeerBottle> {
    @Mapping(target = "beer", source = "beer", qualifiedByName = "beerId")
    @Mapping(target = "fridge", source = "fridge", qualifiedByName = "fridgeId")
    BeerBottleDTO toDto(BeerBottle s);

    @Named("beerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BeerDTO toDtoBeerId(Beer beer);

    @Named("fridgeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FridgeDTO toDtoFridgeId(Fridge fridge);
}
