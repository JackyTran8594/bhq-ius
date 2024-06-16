package com.thanhbinh.dms.domain.dto;

import com.thanhbinh.dms.domain.dto.common.BaseDto;
import com.thanhbinh.dms.domain.entity.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class TechSpecDto extends BaseDto<String> {
    private Long id;

    private String mainboard;

    private String memory;

    private String cpu;

    private String sensor;

    private String measurementRange;

    private String accuracy;

    private String powerSupply;

    private String operating;

    private String weight;

    private String dimension;

    private String other;

    private Long deviceTypeId;

    private String note;
}
