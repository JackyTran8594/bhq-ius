package com.bhq.ius.integration.dto;

import lombok.Data;

@Data
public class ExceptionMoodle {
    private String message;
    private String errorCode;
    private String debugInfo;
}
