package com.thanhbinh.dms.domain.service;

import com.thanhbinh.dms.domain.dto.OrganizationGroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrganizationGroupService {
    Page<OrganizationGroupDto> findBySearchParam(Optional<String> search, Pageable page);
    OrganizationGroupDto create(OrganizationGroupDto dto);
    OrganizationGroupDto update(OrganizationGroupDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    OrganizationGroupDto findById(Long id);


}
