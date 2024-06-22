package com.bhq.ius.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportOneDto {
    public DriverDto driverDto;
    public CourseDto courseDto;
    public ProfileDto profileDto;
    public List<DocumentDto> documentDtos;
}
