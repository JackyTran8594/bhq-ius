package com.thanhbinh.dms.domain.service;

import com.thanhbinh.dms.domain.dto.OrganizationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    Page<OrganizationDto> findBySearchParam(Optional<String> search, Pageable page);
    OrganizationDto create(OrganizationDto dto);
    OrganizationDto update(OrganizationDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    OrganizationDto findById(Long id);


}
