package com.bhq.ius.domain.mapper;

import com.bhq.ius.domain.dto.ProfileDto;
import com.bhq.ius.domain.entity.Profile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);
    Profile toEntity(ProfileDto profileDto);

    ProfileDto toDto(Profile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Profile partialUpdate(ProfileDto profileDto, @MappingTarget Profile profile);

    List<Profile> toEntities(List<ProfileDto> profileDtos);
}