package com.bhq.ius.domain.service.impl;

import com.bhq.ius.constant.RecordState;
import com.bhq.ius.constant.RecordStatus;
import com.bhq.ius.constant.XmlElement;
import com.bhq.ius.domain.dto.*;
import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.repository.CourseRepository;
import com.bhq.ius.domain.repository.DriverRepository;
import com.bhq.ius.domain.repository.custom.DriverSpecificationCustom;
import com.bhq.ius.domain.service.DriverService;
import com.bhq.ius.domain.specification.DriverSpecification;
import com.bhq.ius.domain.specification.DriverSpecificationBuilder;
import com.bhq.ius.domain.specification.GenericSpecificationBuilder;
import com.bhq.ius.domain.specification.criteria.SearchCriteria;
import com.bhq.ius.domain.specification.criteria.SearchOperation;
import com.bhq.ius.utils.DataUtil;
import com.bhq.ius.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository repository;

    @Autowired
    private CourseRepository courseRepository;

//    @Override
//    public Page<DriverDto> findBySearchParam(Optional<String> search, Pageable page, Optional<Long> courseId) {
//        GenericSpecificationBuilder<Driver> builder = new GenericSpecificationBuilder<>();
//        // check chuỗi để tách các param search
//        if (search.isPresent()) {
//            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
//            Matcher matcher = pattern.matcher(search.get());
//            while (matcher.find()) {
//                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
//            }
//        }
//
//        builder.setClazz(Driver.class);
//        Specification<Driver> spec = builder.build();
//        List<DriverDto> listEntities = new ArrayList<>();
//        if (courseId.isPresent()) {
//            listEntities = repository.findAll(spec, page).stream().filter(x -> !DataUtil.isNullOrEmpty(x.getCourse()) && Long.valueOf(7L).equals(x.getCourse().getId())).map(entity -> {
//                DriverDto dto = new DriverDto();
//                BeanUtils.copyProperties(entity, dto);
//                dto.setCourseId(entity.getCourse().getId());
//                dto.setStateProfile(!DataUtil.isNullOrEmpty(entity.getProfile().getState()) ? entity.getProfile().getState() : null);
//                if (!DataUtil.isNullOrEmpty(entity.getProfile().getError())) {
//                    String error = entity.getProfile().getError();
//                    dto.setErrorProfile((error.length() <= 100) ? error : error.substring(0, 100));
//                }
//                dto.setStateEnroll(!DataUtil.isNullOrEmpty(entity.getStateEnroll()) ? entity.getStateEnroll() : null);
//                if (!DataUtil.isNullOrEmpty(entity.getErrorEnroll())) {
//                    dto.setErrorEnroll((entity.getErrorEnroll().length() <= 100) ? entity.getErrorEnroll() : entity.getErrorEnroll().substring(0, 100));
//                }
//
//                return dto;
//            }).toList();
//        } else {
//            listEntities = repository.findAll(spec, page).stream().map(entity -> {
//                DriverDto dto = new DriverDto();
//                BeanUtils.copyProperties(entity, dto);
//                dto.setCourseId(entity.getCourse().getId());
//                dto.setStateProfile(!DataUtil.isNullOrEmpty(entity.getProfile().getState()) ? entity.getProfile().getState() : null);
//                if (!DataUtil.isNullOrEmpty(entity.getProfile().getError())) {
//                    String error = entity.getProfile().getError();
//                    dto.setErrorProfile((error.length() <= 100) ? error : error.substring(0, 100));
//                }
//                dto.setStateEnroll(!DataUtil.isNullOrEmpty(entity.getStateEnroll()) ? entity.getStateEnroll() : null);
//                if (!DataUtil.isNullOrEmpty(entity.getErrorEnroll())) {
//                    dto.setErrorEnroll((entity.getErrorEnroll().length() <= 100) ? entity.getErrorEnroll() : entity.getErrorEnroll().substring(0, 100));
//                }
//                return dto;
//            }).toList();
//        }
//        Page<DriverDto> result = new PageImpl<>(listEntities, page, listEntities.size());
//        return result;
//    }

    @Override
    public Page<DriverDto> findBySearchParam(Optional<String> search, Pageable page, Optional<Long> courseId) {
        DriverSpecificationBuilder builder = new DriverSpecificationBuilder();
        // check chuỗi để tách các param search
        if (search.isPresent()) {
            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search.get() + ",");
            while (matcher.find()) {
                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
            }
        }
        Specification<Driver> spec = builder.build(DriverSpecificationCustom.findAllByCourseId(courseId.get()), courseId.get());
        Page<DriverDto> listDTO = repository.findAll(spec, page).map(entity -> {
            DriverDto dto = new DriverDto();
            BeanUtils.copyProperties(entity, dto);
            dto.setCourseId(entity.getCourse().getId());
            dto.setStateProfile(!DataUtil.isNullOrEmpty(entity.getProfile().getState()) ? entity.getProfile().getState() : null);
            if (!DataUtil.isNullOrEmpty(entity.getProfile().getError())) {
                String error = entity.getProfile().getError();
                dto.setErrorProfile((error.length() <= 100) ? error : error.substring(0, 100));
            }
            dto.setStateEnroll(!DataUtil.isNullOrEmpty(entity.getStateEnroll()) ? entity.getStateEnroll() : null);
            if (!DataUtil.isNullOrEmpty(entity.getErrorEnroll())) {
                dto.setErrorEnroll((entity.getErrorEnroll().length() <= 100) ? entity.getErrorEnroll() : entity.getErrorEnroll().substring(0, 100));
            }
            return dto;
        });
        return listDTO;
    }

    @Override
    public DriverDto create(DriverDto dto) {
        Driver entity = new Driver();
        BeanUtils.copyProperties(dto, entity);
        repository.save(entity);
        return dto;
    }

    @Override
    public DriverDto update(DriverDto dto) {
        Optional<Driver> entity = repository.findById(dto.getId());
        if (entity.isPresent()) {
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
    public DriverDto findById(Long id) {
        DriverDto dto = new DriverDto();
        Optional<Driver> entity = repository.findById(id);
        entity.ifPresent(value -> BeanUtils.copyProperties(value, dto));
        return dto;
    }

    @Override
    public List<Driver> findByListId(List<Long> listId) {
        return repository.findAllById(listId);
    }

    @Override
    public List<Driver> findAll() {
        return repository.findAll();
    }

    @Override
    public Long countByStateSuccess(RecordState state) {
        return repository.countByStateIn(Collections.singletonList(state));
    }

}
