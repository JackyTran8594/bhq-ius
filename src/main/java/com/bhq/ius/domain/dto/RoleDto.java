package com.thanhbinh.dms.domain.dto;

import com.thanhbinh.dms.domain.dto.common.BaseDto;
import com.thanhbinh.dms.domain.entity.Permission;
import com.thanhbinh.dms.domain.entity.RoleGroup;
import com.thanhbinh.dms.domain.entity.User;
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
public class RoleDto extends BaseDto<String> {
    private Long id;

    private String roleCode;

    private String roleName;

    private String description;

    private String note;

    private Long roleGroupId;

}
