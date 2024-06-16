package com.thanhbinh.dms.domain.mapper;

import com.thanhbinh.dms.domain.dto.TechSpecDto;
import com.thanhbinh.dms.domain.entity.TechSpec;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TechSpecMapper {
    TechSpec toEntity(TechSpecDto techSpecDto);

    TechSpecDto toDto(TechSpec techSpec);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TechSpec partialUpdate(TechSpecDto techSpecDto, @MappingTarget TechSpec techSpec);
}