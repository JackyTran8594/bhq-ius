package com.thanhbinh.dms.domain.service.impl;

import com.thanhbinh.dms.domain.dto.DeviceDto;
import com.thanhbinh.dms.domain.dto.OrganizationDto;
import com.thanhbinh.dms.domain.dto.OrganizationGroupDto;
import com.thanhbinh.dms.domain.entity.Device;
import com.thanhbinh.dms.domain.entity.Organization;
import com.thanhbinh.dms.domain.entity.OrganizationGroup;
import com.thanhbinh.dms.domain.repository.OrganizationGroupRepository;
import com.thanhbinh.dms.domain.service.OrganizationGroupService;
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
public class OrganizationGroupServiceImpl implements OrganizationGroupService {

    @Autowired
    private OrganizationGroupRepository repository;
    @Override
    public Page<OrganizationGroupDto> findBySearchParam(Optional<String> search, Pageable page) {
        GenericSpecificationBuilder<OrganizationGroup> builder = new GenericSpecificationBuilder<>();
        // check chuỗi để tách các param search
        if (DataUtil.notNull(search)) {
            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
            }
        }
        // specification
        builder.setClazz(OrganizationGroup.class);
        Specification<OrganizationGroup> spec = builder.build();
        Page<OrganizationGroupDto> listDTO = repository.findAll(spec, page).map(entity -> {
            OrganizationGroupDto dto = new OrganizationGroupDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
        return listDTO;
    }

    @Override
    public OrganizationGroupDto create(OrganizationGroupDto dto) {
        OrganizationGroup entity = new OrganizationGroup();
        BeanUtils.copyProperties(dto, entity);
        repository.save(entity);
        return dto;
    }

    @Override
    public OrganizationGroupDto update(OrganizationGroupDto dto) {
        Optional<OrganizationGroup> entity = repository.findById(dto.getId());
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
    public OrganizationGroupDto findById(Long id) {
        OrganizationGroupDto dto = new OrganizationGroupDto();
        Optional<OrganizationGroup> entity = repository.findById(id);
        entity.ifPresent(value -> BeanUtils.copyProperties(value, dto));
        return dto;
    }
}
