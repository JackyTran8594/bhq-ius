package com.bhq.ius.integration.service;

import com.bhq.ius.constant.IusConstant;
import com.bhq.ius.domain.dto.common.BaseResponseData;
import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.service.DocumentService;
import com.bhq.ius.domain.service.DriverService;
import com.bhq.ius.domain.service.ProfileService;
import com.bhq.ius.domain.service.UserService;
import com.bhq.ius.integration.dto.MoodleCourse;
import com.bhq.ius.integration.dto.MoodleUser;
import com.bhq.ius.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class IntegrationUserServiceImpl implements IntegrationUserSerive {

    @Autowired
    private DriverService driverService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private DocumentService documentService;

    @Value("${app.moodleService.url}")
    private String moodleServiceUrl;

    @Value("${app.moodleService.token}")
    private String moodleServiceToken;

    @Override
    public BaseResponseData<?> CreateDrivers(List<Long> listId) {
        BaseResponseData<String> responseData = new BaseResponseData<>();
        try {
            List<Driver> listDriver = new ArrayList<>();
            if (listId.size() > 0) {
                listDriver = driverService.findByListId(listId);
            } else {
                listDriver = driverService.findAll();
            }


            return responseData;
        } catch (Exception exception) {
            log.error("==== error in CreateUsers ==== {}", exception.getMessage());
            responseData.setError("Có lỗi xảy ra");
            responseData.setStatus(500);
            return responseData;
        }
    }

    @Override
    public BaseResponseData<?> CreateCourses(List<Long> listId) {
//        return responseData;
    }

    private List<MoodleUser> mappingToMoodleUser(List<Driver> listDriver) {
        List<MoodleUser> result = new ArrayList<>();
        for (Driver item : listDriver) {
            MoodleUser user = new MoodleUser();
            user.setUsername(!DataUtil.isNullOrEmpty(item.getMaDK()) ? item.getMaDK() : null);
            user.setFirstName(!DataUtil.isNullOrEmpty(item.getHoTenDem()) ? item.getHoTenDem() : null);
            user.setLastName(!DataUtil.isNullOrEmpty(item.getTen()) ? item.getTen() : null);
            user.setIdNumber(!DataUtil.isNullOrEmpty(item.getMaDK()) ? item.getMaDK() : null);
            user.setEmail(!DataUtil.isNullOrEmpty(item.getHoTenDem()) ? item.getHoTenDem() + IusConstant.EMAIL_PARTERN : null);
            if(!DataUtil.isNullOrEmpty(item.getNgaySinh())) {
                String dateStr = DataUtil.localDateToString(item.getNgaySinh(), IusConstant.DATE_FORMAT);
                String dateEncrypted = DataUtil.encryptPasswordSHA256(dateStr);
                user.setPassword(dateEncrypted);
            }
            result.add(user);
        }
        return result;
    }

    private List<MoodleCourse> mappingToMoodleCourse(List<Course> listCourse) {
        List<MoodleCourse> result = new ArrayList<>();
        for (Course item : listCourse) {
            MoodleCourse course = new MoodleCourse();
            course.setIdNumber(!DataUtil.isNullOrEmpty(item.getMaBCI()) ? item.getMaBCI() : null);
            course.setFullName(!DataUtil.isNullOrEmpty(item.getMaKhoaHoc()) ? item.getMaKhoaHoc() : null);
            course.setShortName(!DataUtil.isNullOrEmpty(item.getTenKhoaHoc()) ? item.getTenKhoaHoc() : null);
            course.setCategory(!DataUtil.isNullOrEmpty(item.getMaHangDaoTao()) ? item.getMaHangDaoTao() : null);
            if(!DataUtil.isNullOrEmpty(item.getNgayKhaiGiang())) {
                long startDate = item.getNgayKhaiGiang().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                course.setStartDate(startDate);
            }
            if(!DataUtil.isNullOrEmpty(item.getNgayBeGiang())) {
                long endDate = item.getNgayBeGiang().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                course.setEndDate(endDate);
            }
        }
        return result;
    }

    private String postUserToMoodleBackend(MoodleUser data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("wstoken", moodleServiceToken);
        headers.add("moodlewsrestformat", "json");
        headers.add("wsfunction", "core_user_create_users");
        headers.add("users[0][username]",data.getUsername());
        headers.add("users[0][password]",data.getPassword());
        headers.add("users[0][firstname]",data.getFirstName());
        headers.add("users[0][lastname]",data.getLastName());
        headers.add("users[0][email]",data.getEmail());
        headers.add("users[0][idnumber]",data.getIdNumber());
        String response = restTemplate.postForObject(moodleServiceUrl, null,  String.class, headers);
        return response;
    }

    private String postCourseToMoodleBackend(MoodleCourse data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("wstoken", moodleServiceToken);
        headers.add("moodlewsrestformat", "json");
        headers.add("wsfunction", "core_course_create_courses");
        headers.add("users[0][fullname ]",data.getFullName());
        headers.add("users[0][shortname]",data.getShortName());
        headers.add("users[0][categoryid]",data.getCategory());
        headers.add("users[0][idnumber]",data.getIdNumber());
        headers.add("users[0][startdate]",data.getStartDate().toString());
        headers.add("users[0][enddate]",data.getEndDate().toString());
        String response = restTemplate.postForObject(moodleServiceUrl, null,  String.class, headers);
        return response;
    }



}
