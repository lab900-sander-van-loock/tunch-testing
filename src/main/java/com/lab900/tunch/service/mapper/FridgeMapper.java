package com.lab900.tunch.service.mapper;

import com.lab900.tunch.domain.Fridge;
import com.lab900.tunch.service.dto.FridgeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Fridge} and its DTO {@link FridgeDTO}.
 */
@Mapper(componentModel = "spring")
public interface FridgeMapper extends EntityMapper<FridgeDTO, Fridge> {}
