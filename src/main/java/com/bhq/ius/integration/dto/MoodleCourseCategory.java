package com.bhq.ius.integration.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class MoodleCourseCategory {
    @JsonSetter("id")
    private Long id;
    @JsonSetter("name")
    private String name;
    @JsonSetter("idnumber")
    private String idNumber;
    @JsonSetter("description")
    private String description;
    @JsonSetter("descriptionformat")
    private Long descriptionFormat;
    @JsonSetter("parent")
    private Long parent;
    @JsonSetter("sortorder")
    private Long sortOrder;
    @JsonSetter("coursecount")
    private Long courseCount;
    @JsonSetter("visible")
    private Long visible;
    @JsonSetter("visibleold")
    private Long visibleOld;
    @JsonSetter("timemodified")
    private Long timeModified;
    @JsonSetter("depth")
    private Long depth;
    @JsonSetter("path")
    private String path;
    @JsonSetter("theme")
    private String theme;
}
