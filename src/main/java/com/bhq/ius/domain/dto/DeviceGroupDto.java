package com.thanhbinh.dms.domain.dto;

import com.thanhbinh.dms.domain.dto.common.BaseDto;
import lombok.*;


/**
 * DTO for {@link com.thanhbinh.dms.domain.entity.DeviceGroup}
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class DeviceGroupDto extends BaseDto<String> {
    public Long id;
    public String typeName;
    public String description;
    public String note;
}