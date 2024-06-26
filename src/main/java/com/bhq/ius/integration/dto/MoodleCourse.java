package com.bhq.ius.integration.dto;

import lombok.Data;

@Data
public class MoodleCourse {
    private String idNumber;
    private String fullName;
    private String shortName;
    private Long categoryId ;
    private Long startDate;
    private Long endDate;
}
