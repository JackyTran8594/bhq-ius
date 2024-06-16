package com.thanhbinh.dms.domain.service;

import com.thanhbinh.dms.domain.dto.DeviceTypeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DeviceTypeService {
    Page<DeviceTypeDto> findBySearchParam(Optional<String> search, Pageable page);
    DeviceTypeDto create(DeviceTypeDto dto);
    DeviceTypeDto update(DeviceTypeDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    DeviceTypeDto findById(Long id);


}
