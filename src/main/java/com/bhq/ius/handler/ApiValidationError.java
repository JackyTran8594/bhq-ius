package com.bhq.ius.handler;

import com.bhq.ius.utils.DataUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiValidationError extends ApiSubError {
    private String object;
    private String field;
    private Object rejectValue;
    private String message;

    public ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public ApiValidationError(String object) {
        this.object = object;
    }

    public ApiValidationError(String object, String field, Object rejectValue, String message) {
        this.object = object;
        this.message = message;
        this.field = field;
        this.rejectValue = (DataUtil.notNull(rejectValue)) ? rejectValue : "null";
    }

}
