package com.bhq.ius.integration.service;

import com.bhq.ius.constant.IusConstant;
import com.bhq.ius.constant.RecordState;
import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.entity.Profile;
import com.bhq.ius.domain.repository.CourseRepository;
import com.bhq.ius.domain.repository.DriverRepository;
import com.bhq.ius.domain.repository.ProfileRepository;
import com.bhq.ius.integration.dto.*;
import com.bhq.ius.utils.DataUtil;
import com.bhq.ius.utils.XmlUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Service
@Slf4j
public class IntegrationUserServiceImpl implements IntegrationUserSerive {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MoodleService moodleService;

    @Override
    public List<Long> CreateDrivers(List<Driver> listDriver) {
        List<Long> listIdSubmitted = new ArrayList<>();
        if (listDriver.size() > 0) {
            for (Driver driver : listDriver) {
                if (!DataUtil.isNullOrEmpty(driver)) {
                    MoodleUser user = new MoodleUser();
                    mappingToMoodleUser(driver, user);
                    try {
                        MoodleUserResponse userResponse = moodleService.postUserToMoodleBackend(user);
                        driver.setState(RecordState.SUBMITTED);
                        driver.setIdUserMoodle(userResponse.getId().toString());
                    } catch (Exception exception) {
                        log.error("=== error in postUserToMoodleBackend === {}", exception.getMessage());
                        driver.setState(RecordState.FAILED);
                        driver.setError(exception.getMessage());
                    }
                    driverRepository.save(driver);
                }
            }
            listIdSubmitted = listDriver.stream().filter(x -> RecordState.SUBMITTED.name().equals(x.getStatus())).map(y -> y.getId()).toList();
        }
        return listIdSubmitted;
    }

    @Override
    public List<Long> CreateCourses(List<Course> listCourse) {
        List<Long> listIdSubmitted = new ArrayList<>();
        if (listCourse.size() > 0) {
            for (Course course : listCourse) {
                if (!DataUtil.isNullOrEmpty(course)) {
                    MoodleCourse moodleCourse = new MoodleCourse();
                    mappingToMoodleCourse(course, moodleCourse);
                    try {
                        /* call to moodle backend that getting categories id */
                        MoodleCourseCategory courseCategoriy = moodleService.getCourseCategoryDetailFromMoodleBackend(IusConstant.COURSE_CATEGORY_IDNUMBER, course.getMaHangDaoTao());
                        moodleCourse.setCategoryId(courseCategoriy.getId());
                        MoodleCourseResponse courseResponse = moodleService.postCourseToMoodleBackend(moodleCourse);
                        course.setState(RecordState.SUBMITTED);
                        course.setIdCourseMoodle(courseResponse.getId().toString());
                        course.setShortNameCourseMoodle(courseResponse.getShortname());

                    } catch (Exception exception) {
                        log.error("=== error in postUserToMoodleBackend === {}", exception.getMessage());
                        course.setState(RecordState.FAILED);
                        course.setError(exception.getMessage());
                    }
                    courseRepository.save(course);
                }
            }
            listIdSubmitted = listCourse.stream().filter(x -> RecordState.SUBMITTED.name().equals(x.getStatus())).map(y -> y.getId()).toList();
        }
        return listIdSubmitted;
    }

    @Override
    public List<Long> UpdateUserPicture(List<Driver> listDriver) {
        List<Long> result = new ArrayList<>();
        if (listDriver.size() > 0) {
            for (Driver item : listDriver) {
                Profile profile = item.getProfile();
                try {
                    /* get token */
                    MoodleTokenMobile token = moodleService.getTokenUserFromMoodle(item.getMaDK(), DataUtil.convertLocalDateToString(item.getNgaySinh()));
                    /* upload file */
                    ByteArrayResource resource = new ByteArrayResource(profile.getImageFile());
                    String fileName = UUID.randomUUID() + "-" + (DataUtil.isNullOrEmpty(item.getMaDK()) ? item.getMaDK() : "temp");
                    MoodleUploadFile uploadFile = moodleService.uploadFileInMoodelWithDedicatedEndpoint(resource, fileName, token.getToken(), item.getIdUserMoodle());
                    /* update picture */
                    moodleService.updateUserPicture(token.getToken(), uploadFile.getItemId(), item.getIdUserMoodle());
                    profile.setState(RecordState.SUBMITTED);
                } catch (Exception exception) {
                    log.error("=== error in UpdateUserPicture === {}", exception.getMessage());
                    profile.setState(RecordState.FAILED);
                    profile.setError(exception.getMessage());
                }
                profileRepository.save(profile);
                result.add(profile.getId());
            }
        }
        return result;
    }

    @Override
    public List<Long> UpdateUserEnroll(List<Driver> listDriver) {
        List<Long> result = new ArrayList<>();
        if (listDriver.size() > 0) {
            for (Driver item : listDriver) {
                Course course = item.getCourse();
                try {
                    moodleService.updateUserEnroll(item.getIdUserMoodle(), course.getIdCourseMoodle());
                    item.setStateEnroll(RecordState.SUBMITTED);
                } catch (Exception exception) {
                    log.error("=== error in UpdateUserPicture === {}", exception.getMessage());
                    item.setStateEnroll(RecordState.FAILED);
                    item.setErrorEnroll(exception.getMessage());
                }
                driverRepository.save(item);
                result.add(item.getId());
            }
        }
        return result;
    }

    @Override
    public void testPostImage() {
        try {
            /* get token */
            MoodleTokenMobile token = moodleService.getTokenUserFromMoodle("06003-20200702-211217", "1982123");
            /* upload file */
            String base64Result = "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";
            byte[] content = Base64.decodeBase64(base64Result);
            ByteArrayResource byteArrayResource = new ByteArrayResource(content);
            MoodleUploadFile uploadFile = moodleService.uploadFileInMoodelWithDedicatedEndpoint(byteArrayResource, UUID.randomUUID() + "06003-20200702-211217", token.getToken(), "41");
            /* update picture */
            moodleService.updateUserPicture(token.getToken(), uploadFile.getItemId(), "41");
        } catch (Exception e) {
            log.error("=== error === {}", e.getMessage());
        }

    }

    /* Utils function */
    private void mappingToMoodleUser(Driver driver, MoodleUser user) {
        user.setUsername(!DataUtil.isNullOrEmpty(driver.getMaDK()) ? driver.getMaDK() : null);
        user.setFirstName(!DataUtil.isNullOrEmpty(driver.getHoTenDem()) ? driver.getHoTenDem() : null);
        user.setLastName(!DataUtil.isNullOrEmpty(driver.getTen()) ? driver.getTen() : null);
        user.setIdNumber(!DataUtil.isNullOrEmpty(driver.getSoCMT()) ? driver.getSoCMT() : null);
        String email = driver.getMaDK() + IusConstant.EMAIL_PARTERN;
        user.setEmail(!DataUtil.isNullOrEmpty(email) ? email : UUID.randomUUID() + IusConstant.EMAIL_PARTERN);
        user.setPassword(!DataUtil.isNullOrEmpty(driver.getNgaySinh()) ? DataUtil.convertLocalDateToString(driver.getNgaySinh()) : null);
    }

    private List<MoodleUser> mappingToListMoodleUser(List<Driver> listDriver) {
        List<MoodleUser> result = new ArrayList<>();
        for (Driver item : listDriver) {
            MoodleUser user = new MoodleUser();
            mappingToMoodleUser(item, user);
            result.add(user);
        }
        return result;
    }

    private void mappingToMoodleCourse(Course item, MoodleCourse course) {
        course.setIdNumber(!DataUtil.isNullOrEmpty(item.getMaBCI()) ? item.getMaBCI() : null);
        course.setFullName(!DataUtil.isNullOrEmpty(item.getMaKhoaHoc()) ? item.getMaKhoaHoc() : null);
        course.setShortName(!DataUtil.isNullOrEmpty(item.getTenKhoaHoc()) ? item.getTenKhoaHoc() : null);
        if (!DataUtil.isNullOrEmpty(item.getNgayKhaiGiang())) {
            long startDate = item.getNgayKhaiGiang().toEpochSecond(LocalTime.NOON, ZoneOffset.MIN);
            ;
            course.setStartDate(startDate);
        }
        if (!DataUtil.isNullOrEmpty(item.getNgayBeGiang())) {
            long endDate = item.getNgayBeGiang().toEpochSecond(LocalTime.NOON, ZoneOffset.MIN);
            course.setEndDate(endDate);
        }
    }

    private List<MoodleCourse> mappingToListMoodleCourse(List<Course> listCourse) {
        List<MoodleCourse> result = new ArrayList<>();
        for (Course item : listCourse) {
            MoodleCourse course = new MoodleCourse();
            mappingToMoodleCourse(item, course);
            result.add(course);
        }
        return result;
    }
    /* End */


}
