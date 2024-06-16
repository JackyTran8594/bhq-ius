package com.thanhbinh.dms.domain.mapper;

import com.thanhbinh.dms.domain.dto.DeviceTypeDto;
import com.thanhbinh.dms.domain.entity.DeviceType;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeviceTypeMapper {
    DeviceType toEntity(DeviceTypeDto deviceTypeDto);

    DeviceTypeDto toDto(DeviceType deviceType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DeviceType partialUpdate(DeviceTypeDto deviceTypeDto, @MappingTarget DeviceType deviceType);
}