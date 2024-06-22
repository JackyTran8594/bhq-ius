package com.bhq.ius.domain.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DriverXmlDto {
    public List<DriverDto> driversDto ;
    public CourseDto courseDto;
    public List<ProfileDto> profilesDto;
    public List<DocumentDto> documentsDto;

}
