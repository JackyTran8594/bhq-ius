package com.bhq.ius.domain.service;

import com.bhq.ius.domain.dto.DriverDto;
import com.bhq.ius.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ReportOneService {
    List<DriverDto> uploadFileXml(MultipartFile multipartFile);

}
