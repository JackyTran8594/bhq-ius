package com.bhq.ius.integration.service;

import com.bhq.ius.domain.entity.Profile;
import com.bhq.ius.integration.dto.MoodleCourse;
import com.bhq.ius.integration.dto.MoodleCourseCategory;
import com.bhq.ius.integration.dto.MoodleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;


public interface MoodleService {
    void postUserToMoodleBackend(MoodleUser user);
    void postCourseToMoodleBackend(MoodleCourse data);
    MoodleCourseCategory getCourseCategoryDetailFromMoodleBackend(String key, String value);
    String uploadFileInMoodelWithDedicatedEndpoint(Profile profile, String tokenForUser);

    String getTokenUserFromMoodle(String username, String password);

    void updateUserPicture(String token, String draftItemId);

}
