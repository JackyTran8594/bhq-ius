package com.thanhbinh.dms.domain.mapper;

import com.thanhbinh.dms.domain.dto.RoleGroupDto;
import com.thanhbinh.dms.domain.entity.RoleGroup;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleGroupMapper {
    RoleGroup toEntity(RoleGroupDto roleGroupDto);

    RoleGroupDto toDto(RoleGroup roleGroup);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoleGroup partialUpdate(RoleGroupDto roleGroupDto, @MappingTarget RoleGroup roleGroup);
}