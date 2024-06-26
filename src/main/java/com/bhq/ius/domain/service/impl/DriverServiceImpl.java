package com.bhq.ius.domain.service.impl;

import com.bhq.ius.constant.XmlElement;
import com.bhq.ius.domain.dto.*;
import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.repository.DriverRepository;
import com.bhq.ius.domain.service.DriverService;
import com.bhq.ius.domain.specification.GenericSpecificationBuilder;
import com.bhq.ius.domain.specification.criteria.SearchCriteria;
import com.bhq.ius.utils.DataUtil;
import com.bhq.ius.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository repository;
    @Override
    public Page<DriverDto> findBySearchParam(Optional<String> search, Pageable page) {
        GenericSpecificationBuilder<Driver> builder = new GenericSpecificationBuilder<>();
        // check chuỗi để tách các param search
        if (DataUtil.notNull(search)) {
            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
            }
        }
        // specification
        builder.setClazz(Driver.class);
        Specification<Driver> spec = builder.build();
        Page<DriverDto> listDTO = repository.findAll(spec, page).map(entity -> {
            DriverDto dto = new DriverDto();
            BeanUtils.copyProperties(entity, dto);
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

}
