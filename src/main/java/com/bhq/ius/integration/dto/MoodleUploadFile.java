package com.bhq.ius.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

@Data
public class MoodleUploadFile {
    @JsonSetter("component")
    private String component;
    @JsonSetter("contextid")
    private Long contextId;
    @JsonSetter("userid")
    private String userId;
    @JsonSetter("filearea")
    private String fileArea;
    @JsonSetter("filename")
    private String fileName;
    @JsonSetter("filepath")
    private String filePath;
    @JsonSetter("itemid")
    private String itemId;
    @JsonSetter("license")
    private String license;
    @JsonSetter("author")
    private String author;
    @JsonSetter("source")
    private String source;
    @JsonSetter("filesize")
    private Long fileSize;
}
