package com.bhq.ius.domain.service;

import com.bhq.ius.domain.dto.CourseDto;
import com.bhq.ius.domain.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Page<CourseDto> findBySearchParam(Optional<String> search, Pageable page);
    CourseDto create(CourseDto dto);
    CourseDto update(CourseDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    CourseDto findById(Long id);
    List<Course> findByListId(List<Long> listId);
    List<Course> findAll();


}
