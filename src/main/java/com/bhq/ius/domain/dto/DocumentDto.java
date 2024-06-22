package com.bhq.ius.domain.dto;

import com.bhq.ius.domain.dto.common.BaseDto;
import lombok.*;

@Data
public class DocumentDto extends BaseDto<String> {
    private Long id;

    private String soCMT;

    private String soCMTCu;

    private String maGiayTo;

    private String tenGiayTo;

}