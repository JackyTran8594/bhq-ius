package com.thanhbinh.dms.domain.service;

import com.thanhbinh.dms.domain.dto.DeviceGroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DeviceGroupService {
    Page<DeviceGroupDto> findBySearchParam(Optional<String> search, Pageable page);
    DeviceGroupDto create(DeviceGroupDto dto);
    DeviceGroupDto update(DeviceGroupDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    DeviceGroupDto findById(Long id);


}
