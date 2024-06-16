package com.thanhbinh.dms.domain.mapper;

import com.thanhbinh.dms.domain.dto.ProductBatchDto;
import com.thanhbinh.dms.domain.entity.ProductBatch;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductBatchMapper {
    ProductBatch toEntity(ProductBatchDto productBatchDto);

    ProductBatchDto toDto(ProductBatch productBatch);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductBatch partialUpdate(ProductBatchDto productBatchDto, @MappingTarget ProductBatch productBatch);
}