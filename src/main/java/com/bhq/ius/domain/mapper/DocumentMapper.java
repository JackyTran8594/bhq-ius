package com.bhq.ius.domain.mapper;

import com.bhq.ius.domain.dto.DocumentDto;
import com.bhq.ius.domain.entity.Document;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);
    Document toEntity(DocumentDto documentDto);

    DocumentDto toDto(Document document);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Document partialUpdate(DocumentDto documentDto, @MappingTarget Document document);

    List<Document> toEntities(List<DocumentDto> documentDtos);

}