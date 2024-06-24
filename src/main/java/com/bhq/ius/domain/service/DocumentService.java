package com.bhq.ius.domain.service;

import com.bhq.ius.domain.dto.DocumentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    Page<DocumentDto> findBySearchParam(Optional<String> search, Pageable page);
    DocumentDto create(DocumentDto dto);
    DocumentDto update(DocumentDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    DocumentDto findById(Long id);







}
