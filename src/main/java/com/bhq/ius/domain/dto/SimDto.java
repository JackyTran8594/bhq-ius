package com.thanhbinh.dms.domain.dto;

import com.thanhbinh.dms.domain.dto.common.BaseDto;
import com.thanhbinh.dms.domain.entity.Device;
import jakarta.persistence.*;
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
public class SimDto extends BaseDto<String> {
    private Long id;

    private String serialNumber;

    private String provider;

    private BigDecimal value;

    private LocalDateTime activedDate;

    private String note;

}
