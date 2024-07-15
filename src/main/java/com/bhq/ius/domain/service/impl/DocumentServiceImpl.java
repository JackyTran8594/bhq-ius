package com.bhq.ius.domain.service.impl;

import com.bhq.ius.domain.dto.DocumentDto;
import com.bhq.ius.domain.entity.Document;
import com.bhq.ius.domain.repository.DocumentRepository;
import com.bhq.ius.domain.service.DocumentService;
import com.bhq.ius.domain.specification.GenericSpecificationBuilder;
import com.bhq.ius.domain.specification.criteria.SearchCriteria;
import com.bhq.ius.utils.DataUtil;
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
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository repository;

    @Override
    public Page<DocumentDto> findBySearchParam(Optional<String> search, Pageable page) {
        GenericSpecificationBuilder<Document> builder = new GenericSpecificationBuilder<>();
        // check chuỗi để tách các param search
        if (DataUtil.notNull(search)) {
            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search.get() + ",");
            while (matcher.find()) {
                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
            }
        }
        // specification
        builder.setClazz(Document.class);
        Specification<Document> spec = builder.build();
        Page<DocumentDto> listDTO = repository.findAll(spec, page).map(entity -> {
            DocumentDto dto = new DocumentDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
        return listDTO;
    }

    @Override
    public DocumentDto create(DocumentDto dto) {
        Document entity = new Document();
        BeanUtils.copyProperties(dto, entity);
        repository.save(entity);
        return dto;
    }

    @Override
    public DocumentDto update(DocumentDto dto) {
        Optional<Document> entity = repository.findById(dto.getId());
        if (entity.isPresent()) {
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
    public DocumentDto findById(Long id) {
        DocumentDto dto = new DocumentDto();
        Optional<Document> entity = repository.findById(id);
        entity.ifPresent(value -> BeanUtils.copyProperties(value, dto));
        return dto;
    }
}
