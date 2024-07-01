package com.bhq.ius.domain.dto.common;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
public class BaseResponseData<T> {
    public String error;
    public String message;
    public Integer status;
    public LocalDateTime timestamp;
    public T data;
    public Page<T> pagingData;

    public BaseResponseData() {
        this.timestamp = LocalDateTime.now();
    }

    public void success() {
        this.message = "Successful";
        this.status = HttpStatus.OK.value();
    }

    public void initData(T data) {
        this.data = data;
        this.success();
    }

    public BaseResponseData(HttpStatusCode httpStatusCode, String errorMessage) {
        this.status = httpStatusCode.value();
        this.error = errorMessage;
    }

    public void error(HttpStatus httpStatus, String errorMessage) {
        this.status = httpStatus.value();
        this.error = errorMessage;
    }
}
