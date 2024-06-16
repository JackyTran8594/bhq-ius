package com.thanhbinh.dms.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thanhbinh.dms.domain.dto.common.BaseDto;
import com.thanhbinh.dms.domain.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionDto extends BaseDto<String> {
    private Long id;

    private String code;

    private String name;

    private String url;

    private String method;

    private String parentCode;

    private Boolean isAction;

    private String description;

    private String note;

}
