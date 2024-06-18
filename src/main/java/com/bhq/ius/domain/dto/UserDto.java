package com.bhq.ius.domain.dto;

import com.bhq.ius.domain.dto.common.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDto extends BaseDto<String> {
    private Long id;

    private String username;

    private String password;

    private String fullName;

    private String phone;

    private String email;

    private LocalDateTime lastLoginTime;

    private Long roleId;

}
