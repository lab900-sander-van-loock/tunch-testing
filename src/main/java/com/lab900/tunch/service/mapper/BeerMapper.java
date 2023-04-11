package com.lab900.tunch.service.mapper;

import com.lab900.tunch.domain.Beer;
import com.lab900.tunch.service.dto.BeerDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Beer} and its DTO {@link BeerDTO}.
 */
@Mapper(componentModel = "spring")
public interface BeerMapper extends EntityMapper<BeerDTO, Beer> {}
