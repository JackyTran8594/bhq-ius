package com.thanhbinh.dms.domain.mapper;

import com.thanhbinh.dms.domain.dto.DeviceGroupDto;
import com.thanhbinh.dms.domain.entity.DeviceGroup;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeviceGroupMapper {
    DeviceGroup toEntity(DeviceGroupDto deviceGroupDto);

    DeviceGroupDto toDto(DeviceGroup deviceGroup);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DeviceGroup partialUpdate(DeviceGroupDto deviceGroupDto, @MappingTarget DeviceGroup deviceGroup);
}