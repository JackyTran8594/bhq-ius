package com.bhq.ius.domain.service.impl;

import com.bhq.ius.domain.dto.CourseDto;
import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.repository.CourseRepository;
import com.bhq.ius.domain.service.CourseService;
import com.bhq.ius.domain.specification.GenericSpecificationBuilder;
import com.bhq.ius.domain.specification.criteria.SearchCriteria;
import com.bhq.ius.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository repository;
    @Override
    public Page<CourseDto> findBySearchParam(Optional<String> search, Pageable page) {
        GenericSpecificationBuilder<Course> builder = new GenericSpecificationBuilder<>();
        // check chuỗi để tách các param search
        if (DataUtil.notNull(search)) {
            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
            }
        }
        // specification
        builder.setClazz(Course.class);
        Specification<Course> spec = builder.build();
        Page<CourseDto> listDTO = repository.findAll(spec, page).map(entity -> {
            CourseDto dto = new CourseDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
        return listDTO;
    }

    @Override
    public CourseDto create(CourseDto dto) {
        Course entity = new Course();
        BeanUtils.copyProperties(dto, entity);
        repository.save(entity);
        return dto;
    }

    @Override
    public CourseDto update(CourseDto dto) {
        Optional<Course> entity = repository.findById(dto.getId());
        if(entity.isPresent()) {
            BeanUtils.copyProperties(dto, entity);
            repository.save(entity.get());
        }
        return dto;
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByListId(List<Long> listId) {
        repository.deleteAllById(listId);
    }

    @Override
    public CourseDto findById(Long id) {
        CourseDto dto = new CourseDto();
        Optional<Course> entity = repository.findById(id);
        entity.ifPresent(value -> BeanUtils.copyProperties(value, dto));
        return dto;
    }

    @Override
    public List<Course> findByListId(List<Long> listId) {
        return null;
    }

    @Override
    public List<Course> findAll() {
        return null;
    }
}
