package com.thanhbinh.dms.domain.dto.common;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

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
}
