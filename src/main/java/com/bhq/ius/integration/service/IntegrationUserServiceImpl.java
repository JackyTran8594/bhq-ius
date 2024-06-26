package com.bhq.ius.integration.service;

import com.bhq.ius.constant.IusConstant;
import com.bhq.ius.constant.RecordStatus;
import com.bhq.ius.constant.XmlElement;
import com.bhq.ius.domain.dto.ProfileDto;
import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.repository.CourseRepository;
import com.bhq.ius.domain.repository.DriverRepository;
import com.bhq.ius.integration.dto.ExceptionMoodle;
import com.bhq.ius.integration.dto.MoodleCourse;
import com.bhq.ius.integration.dto.MoodleCourseCategoriy;
import com.bhq.ius.integration.dto.MoodleUser;
import com.bhq.ius.utils.DataUtil;
import com.bhq.ius.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class IntegrationUserServiceImpl implements IntegrationUserSerive {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Value("${app.moodleService.url}")
    private String moodleServiceUrl;

    @Value("${app.moodleService.token}")
    private String moodleServiceToken;

    @Override
    public List<Long> CreateDrivers(List<Driver> listDriver) {
        List<Long> listIdSubmitted = new ArrayList<>();
        if (listDriver.size() > 0) {
            for (Driver driver : listDriver) {
                if (!DataUtil.isNullOrEmpty(driver)) {
                    MoodleUser user = new MoodleUser();
                    mappingToMoodleUser(driver, user);
                    try {
                        postUserToMoodleBackend(user);
                        driver.setStatus(RecordStatus.SUBMITTED.name());
                    } catch (Exception exception) {
                        log.error("=== error in postUserToMoodleBackend === {}", exception.getMessage());
                        driver.setStatus(RecordStatus.FAILED.name());
                        driver.setNote(exception.getMessage());
                    }
                    driverRepository.save(driver);
                }
            }
            listIdSubmitted = listDriver.stream().filter(x -> RecordStatus.SUBMITTED.name().equals(x.getStatus())).map(y -> y.getId()).toList();
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
                        MoodleCourseCategoriy courseCategoriy = getCourseCategoryDetailFromMoodleBackend(IusConstant.COURSE_CATEGORY_IDNUMBER, course.getMaHangDaoTao());
                        moodleCourse.setCategoryId(courseCategoriy.getId());
                        postCourseToMoodleBackend(moodleCourse);
                        course.setStatus(RecordStatus.SUBMITTED.name());
                    } catch (Exception exception) {
                        log.error("=== error in postUserToMoodleBackend === {}", exception.getMessage());
                        course.setStatus(RecordStatus.FAILED.name());
                        course.setNote(exception.getMessage());
                    }
                    courseRepository.save(course);
                }
            }
            listIdSubmitted = listCourse.stream().filter(x -> RecordStatus.SUBMITTED.name().equals(x.getStatus())).map(y -> y.getId()).toList();
        }
        return listIdSubmitted;
    }

    private void mappingToMoodleUser(Driver driver, MoodleUser user) {
        user.setUsername(!DataUtil.isNullOrEmpty(driver.getMaDK()) ? driver.getMaDK() : null);
        user.setFirstName(!DataUtil.isNullOrEmpty(driver.getHoTenDem()) ? driver.getHoTenDem() : null);
        user.setLastName(!DataUtil.isNullOrEmpty(driver.getTen()) ? driver.getTen() : null);
        user.setIdNumber(!DataUtil.isNullOrEmpty(driver.getMaDK()) ? driver.getMaDK() : null);
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
        course.setCategory(!DataUtil.isNullOrEmpty(item.getMaHangDaoTao()) ? item.getMaHangDaoTao() : null);
        if (!DataUtil.isNullOrEmpty(item.getNgayKhaiGiang())) {
            long startDate = item.getNgayKhaiGiang().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            course.setStartDate(startDate);
        }
        if (!DataUtil.isNullOrEmpty(item.getNgayBeGiang())) {
            long endDate = item.getNgayBeGiang().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
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

    private void postUserToMoodleBackend(MoodleUser data) {
        /* setting resttemplate */
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        /* setting headers */
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "application/xml");
        headers.set("Accept-Charset", "utf-8");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        /* keeping param with order by linkedHashMap */
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("wstoken", moodleServiceToken.trim());
        params.put("moodlewsrestformat", "json");
        params.put("wsfunction", "core_user_create_users");
        params.put("users[0][username]", data.getUsername());
        params.put("users[0][password]", data.getPassword());
        params.put("users[0][firstname]", data.getFirstName());
        params.put("users[0][lastname]", data.getLastName());
        params.put("users[0][email]", data.getEmail());
        params.put("users[0][idnumber]", data.getIdNumber());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moodleServiceUrl.trim());

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        log.info("=== uri === {}", builder.toUriString());
        HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class, headers);
        log.info("==== response from moodle backend - create user ==== {}", response);
        log.info("==== body from response from moodle backend - create user ==== {}", response.getBody());
        if(response.getBody().contains("exception")) {
            ExceptionMoodle exception = DataUtil.jsonToObject(response.getBody(), ExceptionMoodle.class);
            throw new RuntimeException(exception.getMessage());
        }
    }
    private void postCourseToMoodleBackend(MoodleCourse data) {
        /* setting resttemplate */
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        /* setting headers */
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "application/xml");
        headers.set("Accept-Charset", "utf-8");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        /* keeping param with order by linkedHashMap */
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("wstoken", moodleServiceToken.trim());
        params.put("moodlewsrestformat", "json");
        params.put("wsfunction", "core_course_create_courses");
        params.put("courses[0][fullname]", data.getFullName());
        params.put("courses[0][shortname]", data.getShortName());
        params.put("courses[0][categoryid]", data.getCategory());
        params.put("courses[0][idnumber]", data.getIdNumber());
        params.put("courses[0][startdate]", data.getStartDate().toString());
        params.put("courses[0][enddate]", data.getEndDate().toString());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moodleServiceUrl.trim());

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        log.info("=== uri === {}", builder.toUriString());
        HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class, headers);
        log.info("==== response from moodle backend - create course ==== {}", response);
        log.info("==== body from response from moodle backend - create course ==== {}", response.getBody());
        if(response.getBody().contains("exception")) {
            ExceptionMoodle exception = DataUtil.jsonToObject(response.getBody(), ExceptionMoodle.class);
            throw new RuntimeException(exception.getMessage());
        }

    }

    /**
     * get course category detail from moodle
     * @param key : column in mdl_course_categories
     * @param value: value match it
     * @return
     */
    private MoodleCourseCategoriy getCourseCategoryDetailFromMoodleBackend(String key, String value) {
        /* setting resttemplate */
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        /* setting headers */
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "application/xml");
        headers.set("Accept-Charset", "utf-8");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        /* keeping param with order by linkedHashMap */
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("wstoken", moodleServiceToken.trim());
        params.put("moodlewsrestformat", "json");
        params.put("wsfunction", "core_course_get_categories");
        params.put("criteria[0][key]", key);
        params.put("criteria[0][value]", value);


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moodleServiceUrl.trim());

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        log.info("=== uri === {}", builder.toUriString());
        HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class, headers);
        log.info("==== response from moodle backend - get course categories ==== {}", response);
        log.info("==== body from response from moodle backend - get course categories ==== {}", response.getBody());
        if(response.getBody().contains("exception")) {
            ExceptionMoodle exception = DataUtil.jsonToObject(response.getBody(), ExceptionMoodle.class);
            throw new RuntimeException(exception.getMessage());
        }
        MoodleCourseCategoriy courseCategoriy = DataUtil.jsonToObject(response.getBody(), MoodleCourseCategoriy.class);
        return courseCategoriy;
    }

    private ExceptionMoodle convertException(String response) {
        ExceptionMoodle dto = new ExceptionMoodle();
        try {
            Element element = XmlUtil.convertStringToNode(response);
            NodeList nodeList = element.getChildNodes();
            log.info("==== nodeList.getLength() ==== {}", nodeList.getLength());
            for (int j = 0; j < nodeList.getLength(); j++) {
                Node childNode = nodeList.item(j);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementChild = (Element) childNode;
                    if ("MESSAGE".equals(childNode.getNodeName())) {
                        dto.setMessage(XmlUtil.getNodeValue(elementChild));
                    }
                    if ("ERRORCODE".equals(childNode.getNodeName())) {
                        dto.setErrorCode(XmlUtil.getNodeValue(elementChild));
                    }
                    if ("DEBUGINFO".equals(childNode.getNodeName())) {
                        dto.setDebugInfo(XmlUtil.getNodeValue(elementChild));
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }


}
