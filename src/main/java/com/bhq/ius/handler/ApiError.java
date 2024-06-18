package com.bhq.ius.handler;

import com.bhq.ius.utils.DataUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.ConstraintViolation;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
public class ApiError {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime localDateTime;
    private String message;
    private String debugMessage;
    private String detailMessage;
    private List<ApiSubError> subErrors;
    public ApiError() {
        this.localDateTime = LocalDateTime.now();
    };

    public ApiError(HttpStatus httpStatus) {
        this.status = httpStatus;
    }

    public ApiError(HttpStatus httpStatus, Throwable ex) {
        this.status = httpStatus;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus httpStatus, String message, Throwable ex) {
        this.status = httpStatus;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    private void addSubErrors(ApiSubError error) {
        this.subErrors = new ArrayList<>();
        if(DataUtil.isNullOrEmpty(error)) {
            subErrors.add(error);
        }
    }

    private void addValidationError(String object, String field, Object rejectValue, String message) {
        addSubErrors(new ApiValidationError(object, field, rejectValue, message));
    }

    protected void addValidationError(String object, String message) {
        addSubErrors(new ApiValidationError(object, message));
    }

    /**
     * using for SQL exception
     * @param object
     */
    protected void addValidationError(String object) {
        addSubErrors(new ApiValidationError(object));
    }

    private void addValidationError(FieldError fieldError) {

        this.addValidationError(fieldError.getObjectName()
                , fieldError.getField()
                , fieldError.getRejectedValue()
                , fieldError.getDefaultMessage());
    }

    protected void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    protected void addValidationError(ObjectError objectError) {
        if (DataUtil.notNull(objectError)) {
            this.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
        }
    }


    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param constraintViolation
     */

    private void addValidationError(ConstraintViolation<?> constraintViolation) {
        this.addValidationError(
                constraintViolation.getRootBeanClass().getSimpleName(),
                ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().asString(),
                constraintViolation.getInvalidValue(),
                constraintViolation.getMessage()
        );
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }


}
