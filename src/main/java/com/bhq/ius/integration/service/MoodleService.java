package com.bhq.ius.integration.service;

import com.bhq.ius.domain.entity.Profile;
import com.bhq.ius.integration.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.File;


public interface MoodleService {
    MoodleUserResponse postUserToMoodleBackend(MoodleUser user);
    void postCourseToMoodleBackend(MoodleCourse data);
    MoodleCourseCategory getCourseCategoryDetailFromMoodleBackend(String key, String value);
    MoodleUploadFile uploadFileInMoodelWithDedicatedEndpoint(ByteArrayResource contentsAsResource, String fileName , String tokenForUser, Long userId);
    MoodleTokenMobile getTokenUserFromMoodle(String username, String password);
    void updateUserPicture(String token, String draftItemId, Long userId);

}
