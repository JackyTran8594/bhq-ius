package com.thanhbinh.dms.domain.dto;

import com.thanhbinh.dms.domain.dto.common.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class OrganizationDto extends BaseDto<String> {
    private Long id;

    private String name;

    private String address;

    private Long orgGroupId;

    private Long parentOrgId;

    private String description;

    private String note;

}
