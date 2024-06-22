package com.bhq.ius.domain.mapper;

import com.bhq.ius.domain.dto.DriverDto;
import com.bhq.ius.domain.entity.Driver;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DriverMapper {

    DriverMapper INSTANCE = Mappers.getMapper(DriverMapper.class);
    Driver toEntity(DriverDto driverDto);

    DriverDto toDto(Driver driver);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Driver partialUpdate(DriverDto driverDto, @MappingTarget Driver driver);

    List<Driver> toEntities(List<DriverDto> driverDtos);

    List<DriverDto> toListDto(List<Driver> drivers);
}