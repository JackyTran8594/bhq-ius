package com.thanhbinh.dms.domain.mapper;

import com.thanhbinh.dms.domain.dto.SimDto;
import com.thanhbinh.dms.domain.entity.Sim;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SimMapper {
    Sim toEntity(SimDto simDto);

    SimDto toDto(Sim sim);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Sim partialUpdate(SimDto simDto, @MappingTarget Sim sim);
}