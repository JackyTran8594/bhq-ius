package com.thanhbinh.dms.domain.service;

import com.thanhbinh.dms.domain.dto.DeviceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    Page<DeviceDto> findBySearchParam(Optional<String> search, Pageable page);
    DeviceDto createDevice(DeviceDto dto);
    DeviceDto updateDevice(DeviceDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    void ImportBatchDevice(List<DeviceDto> listDeviceDto);
    DeviceDto findById(Long id);


}
