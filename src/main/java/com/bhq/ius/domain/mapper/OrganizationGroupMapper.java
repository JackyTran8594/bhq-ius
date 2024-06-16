package com.thanhbinh.dms.domain.mapper;

import com.thanhbinh.dms.domain.dto.OrganizationGroupDto;
import com.thanhbinh.dms.domain.entity.OrganizationGroup;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationGroupMapper {
    OrganizationGroup toEntity(OrganizationGroupDto organizationGroupDto);

    OrganizationGroupDto toDto(OrganizationGroup organizationGroup);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrganizationGroup partialUpdate(OrganizationGroupDto organizationGroupDto, @MappingTarget OrganizationGroup organizationGroup);
}