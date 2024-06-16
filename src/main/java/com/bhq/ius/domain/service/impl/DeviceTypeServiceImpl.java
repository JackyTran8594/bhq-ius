package com.thanhbinh.dms.domain.service.impl;

import com.thanhbinh.dms.domain.dto.DeviceTypeDto;
import com.thanhbinh.dms.domain.entity.DeviceType;
import com.thanhbinh.dms.domain.repository.DeviceTypeRepository;
import com.thanhbinh.dms.domain.service.DeviceTypeService;
import com.thanhbinh.dms.domain.specification.GenericSpecificationBuilder;
import com.thanhbinh.dms.domain.specification.criteria.SearchCriteria;
import com.thanhbinh.dms.utils.DataUtil;
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
public class DeviceTypeServiceImpl implements DeviceTypeService {

    @Autowired
    private DeviceTypeRepository repository;
    @Override
    public Page<DeviceTypeDto> findBySearchParam(Optional<String> search, Pageable page) {
        GenericSpecificationBuilder<DeviceType> builder = new GenericSpecificationBuilder<>();
        // check chuỗi để tách các param search
        if (DataUtil.notNull(search)) {
            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
            }
        }
        // specification
        builder.setClazz(DeviceType.class);
        Specification<DeviceType> spec = builder.build();
        Page<DeviceTypeDto> listDTO = repository.findAll(spec, page).map(entity -> {
            DeviceTypeDto dto = new DeviceTypeDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
        return listDTO;
    }

    @Override
    public DeviceTypeDto create(DeviceTypeDto dto) {
        DeviceType entity = new DeviceType();
        BeanUtils.copyProperties(dto, entity);
        repository.save(entity);
        return dto;
    }

    @Override
    public DeviceTypeDto update(DeviceTypeDto dto) {
        Optional<DeviceType> entity = repository.findById(dto.getId());
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
    public DeviceTypeDto findById(Long id) {
        DeviceTypeDto dto = new DeviceTypeDto();
        Optional<DeviceType> entity = repository.findById(id);
        entity.ifPresent(value -> BeanUtils.copyProperties(value, dto));
        return dto;
    }
}
