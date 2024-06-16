package com.thanhbinh.dms.domain.mapper;

import com.thanhbinh.dms.domain.dto.PermissionDto;
import com.thanhbinh.dms.domain.entity.Permission;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {
    Permission toEntity(PermissionDto permissionDto);

    PermissionDto toDto(Permission permission);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Permission partialUpdate(PermissionDto permissionDto, @MappingTarget Permission permission);
}