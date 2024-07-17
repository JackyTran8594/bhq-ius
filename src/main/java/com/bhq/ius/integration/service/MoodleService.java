package com.bhq.ius.integration.service;

import com.bhq.ius.domain.entity.Profile;
import com.bhq.ius.integration.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


public interface MoodleService {
    MoodleUserResponse postUserToMoodleBackend(MoodleUser user);
    MoodleCourseResponse postCourseToMoodleBackend(MoodleCourse data);
    MoodleCourseCategory getCourseCategoryDetailFromMoodleBackend(String key, String value);
    MoodleUploadFile uploadFileInMoodelWithDedicatedEndpoint(ByteArrayResource byteArrayResource, String fileName , String tokenForUser, String userId);
    MoodleTokenMobile getTokenUserFromMoodle(String username, String password);
    void updateUserEnroll(String userId, String courseId);
    void updateUserPicture(String token, String draftItemId, String userId);

}
