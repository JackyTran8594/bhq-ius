package com.bhq.ius.integration.dto;

import lombok.Data;

@Data
public class MoodleCourseCategoriy {
    private Long id;
    private String name;
    private String idNumber;
    private String description;
    private Long descriptionFormat;
    private Long parent;
    private Long sortOrder;
    private Long courseCount;
    private Long visible;
    private Long visibleOld;
    private Long timeModified;
    private Long depth;
    private String path;
    private String theme;
}
