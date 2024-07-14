package com.bhq.ius.api;

import com.bhq.ius.domain.dto.DriverDto;
import com.bhq.ius.domain.dto.DriverXmlDto;
import com.bhq.ius.domain.dto.UserDto;
import com.bhq.ius.domain.dto.common.BaseResponseData;
import com.bhq.ius.domain.service.ReportOneService;
import com.bhq.ius.domain.service.UserService;
import com.bhq.ius.utils.CommonUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/private/api/v1/report-one")
public class ReportOneController {

    @Autowired
    private ReportOneService service;

    @PostMapping(value = "/upload-xml")
    public ResponseEntity<BaseResponseData<List<DriverDto>>> uploadXml(@RequestParam("file") MultipartFile file) {
        BaseResponseData<List<DriverDto>> response = new BaseResponseData<>();
        List<DriverDto> dto = service.uploadFileXml(file);
        response.initData(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/submit-driver")
    public ResponseEntity<BaseResponseData<?>> submitDriver(@RequestBody List<Long> listId) {
        BaseResponseData<?> response = service.submitDriver(listId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/test-get-driver")
    public ResponseEntity<BaseResponseData<?>> testGetDriver(@RequestBody List<Long> listId) {
        BaseResponseData<?> response = service.testGetDriver(listId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/submit-course")
    public ResponseEntity<BaseResponseData<?>> submitCourse(@RequestBody List<Long> listId) {
        BaseResponseData<?> response = service.submitCourse(listId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/submit-avatar")
    public ResponseEntity<BaseResponseData<?>> submitAvatar(@RequestBody List<Long> listId) {
        BaseResponseData<?> response = service.submitAvatar(listId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/submit-enroll")
    public ResponseEntity<BaseResponseData<?>> submitEnroll(@RequestBody List<Long> listId) {
        BaseResponseData<?> response = service.submitEnroll(listId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/test-image")
    public ResponseEntity<BaseResponseData<?>> testPostImage() {
        BaseResponseData<?> response = service.testPostImage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
