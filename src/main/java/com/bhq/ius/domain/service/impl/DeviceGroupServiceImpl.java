package com.thanhbinh.dms.domain.service.impl;

import com.thanhbinh.dms.domain.dto.DeviceGroupDto;
import com.thanhbinh.dms.domain.entity.DeviceGroup;
import com.thanhbinh.dms.domain.repository.DeviceGroupRepository;
import com.thanhbinh.dms.domain.service.DeviceGroupService;
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
public class DeviceGroupServiceImpl implements DeviceGroupService {

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Override
    public Page<DeviceGroupDto> findBySearchParam(Optional<String> search, Pageable page) {
        GenericSpecificationBuilder<DeviceGroup> builder = new GenericSpecificationBuilder<>();
        // check chuỗi để tách các param search
        if (DataUtil.notNull(search)) {
            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
            }
        }
        // specification
        builder.setClazz(DeviceGroup.class);
        Specification<DeviceGroup> spec = builder.build();
        Page<DeviceGroupDto> listDTO = deviceGroupRepository.findAll(spec, page).map(entity -> {
            DeviceGroupDto dto = new DeviceGroupDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
        return listDTO;
    }

    @Override
    public DeviceGroupDto create(DeviceGroupDto dto) {
        DeviceGroup entity = new DeviceGroup();
        BeanUtils.copyProperties(dto, entity);
        deviceGroupRepository.save(entity);
        return dto;
    }

    @Override
    public DeviceGroupDto update(DeviceGroupDto dto) {
        Optional<DeviceGroup> deviceGroup = deviceGroupRepository.findById(dto.getId());
        if (deviceGroup.isPresent()) {
            BeanUtils.copyProperties(dto, deviceGroup);
            deviceGroupRepository.save(deviceGroup.get());
        }
        return dto;
    }

    @Override
    public void deleteById(Long id) {
        deviceGroupRepository.deleteById(id);
    }

    @Override
    public void deleteByListId(List<Long> listId) {
        deviceGroupRepository.deleteAllById(listId);
    }

    @Override
    public DeviceGroupDto findById(Long id) {
        DeviceGroupDto dto = new DeviceGroupDto();
        Optional<DeviceGroup> entity = deviceGroupRepository.findById(id);
        entity.ifPresent(value -> BeanUtils.copyProperties(value, dto));
        return dto;
    }
}
