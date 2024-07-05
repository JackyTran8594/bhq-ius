package com.bhq.ius.integration.dto;

import lombok.Data;

@Data
public class MoodleUploadFile {
    private String component;
    private Long contextId;
    private String userId;
    private String fileArea;
    private String fileName;
    private String filePath;
    private String itemId;
    private String license;
    private String author;
    private String source;
    private Long fileSize;
}
