package com.bhq.ius.domain.mapper;

import com.bhq.ius.domain.dto.CourseDto;
import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.integration.dto.MoodleCourse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);
    Course toEntity(CourseDto courseDto);

    CourseDto toDto(Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Course partialUpdate(CourseDto courseDto, @MappingTarget Course course);
}