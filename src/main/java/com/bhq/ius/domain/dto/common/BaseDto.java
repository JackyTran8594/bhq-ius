package com.thanhbinh.dms.domain.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseDto<T> {
    protected String createdBy;

    protected LocalDateTime createdDate;

    protected String updatedBy;

    protected LocalDateTime updatedDate;
    protected T status;
}
