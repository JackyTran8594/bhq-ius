package com.thanhbinh.dms.domain.dto;

import com.thanhbinh.dms.domain.dto.common.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductBatchDto extends BaseDto<String> {
    private Long id;

    private Long batchNumber;

    private String name;

    private String description;

    private BigDecimal value;

    private LocalDateTime batchDate;

    private String note;

}
