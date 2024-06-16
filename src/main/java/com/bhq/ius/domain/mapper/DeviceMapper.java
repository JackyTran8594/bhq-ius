package com.thanhbinh.dms.domain.mapper;

import com.thanhbinh.dms.domain.dto.DeviceDto;
import com.thanhbinh.dms.domain.entity.Device;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeviceMapper {
    Device toEntity(DeviceDto deviceDTO);

    DeviceDto toDto(Device device);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Device partialUpdate(DeviceDto deviceDTO, @MappingTarget Device device);
}