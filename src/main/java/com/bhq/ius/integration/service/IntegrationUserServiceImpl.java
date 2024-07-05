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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
                        moodleService.postUserToMoodleBackend(user);
                        driver.setState(RecordState.SUBMITTED);
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
                        moodleService.postCourseToMoodleBackend(moodleCourse);
                        course.setState(RecordState.SUBMITTED);
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
                    MoodleUploadFile uploadFile = moodleService.uploadFileInMoodelWithDedicatedEndpoint(resource, resource.getFilename(), token.getToken(), item.getIdUserMoodle());
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
