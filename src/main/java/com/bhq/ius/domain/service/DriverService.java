package com.bhq.ius.domain.service;

import com.bhq.ius.constant.RecordState;
import com.bhq.ius.domain.dto.DriverDto;
import com.bhq.ius.domain.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    Page<DriverDto> findBySearchParam(Optional<String> search, Pageable page, Optional<Long> courseId);
    DriverDto create(DriverDto dto);
    DriverDto update(DriverDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    DriverDto findById(Long id);
    List<Driver> findByListId(List<Long> listId);
    List<Driver> findAll();
    Long countByStateSuccess(RecordState state);



}
