package com.bhq.ius.integration.service;

import com.bhq.ius.domain.entity.Profile;
import com.bhq.ius.integration.dto.*;
import com.bhq.ius.utils.DataUtil;
import com.bhq.ius.utils.XmlUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MoodleServiceImpl implements MoodleService {


    @Value("${app.moodleService.url}")
    private String moodleServiceUrl;

    /* using for upload avatar*/
    @Value("${app.moodleService.dedicated-endpoint}")
    private String moodleServiceDedicatedEnpointUrl;

    @Value("${app.moodleService.token}")
    private String moodleServiceToken;

    @Value("${app.moodleService.login-endpoint}")
    private String moodleServiceLoginEndpoint;
    @Override
    public MoodleUserResponse postUserToMoodleBackend(MoodleUser user) {
        /* setting resttemplate */
        RestTemplate restTemplate = buildingDefaultResTemplate();
        /* setting headers */
        HttpHeaders headers = buildingDefaultHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        /* keeping param with order by linkedHashMap */
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("wstoken", moodleServiceToken.trim());
        params.put("moodlewsrestformat", "json");
        params.put("wsfunction", "core_user_create_users");
        params.put("users[0][username]", user.getUsername());
        params.put("users[0][password]", user.getPassword());
        params.put("users[0][firstname]", user.getFirstName());
        params.put("users[0][lastname]", user.getLastName());
        params.put("users[0][email]", user.getEmail());
        params.put("users[0][idnumber]", user.getIdNumber());

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
        List<MoodleUserResponse> listUser = convertJsonToListUserResponse(response.getBody());
        MoodleUserResponse userResponse = (listUser.size() > 0) ? listUser.get(0) : new MoodleUserResponse();
        return userResponse;
    }

    @Override
    public MoodleCourseResponse postCourseToMoodleBackend(MoodleCourse data) {
        /* setting resttemplate */
        RestTemplate restTemplate = buildingDefaultResTemplate();
        /* setting headers */
        HttpHeaders headers = buildingDefaultHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        /* keeping param with order by linkedHashMap */
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("wstoken", moodleServiceToken.trim());
        params.put("moodlewsrestformat", "json");
        params.put("wsfunction", "core_course_create_courses");
        params.put("courses[0][fullname]", data.getFullName());
        params.put("courses[0][shortname]", data.getShortName());
        params.put("courses[0][categoryid]", data.getCategoryId().toString());
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
        List<MoodleCourseResponse> listCourse = convertJsonToListCourseResponse(response.getBody());
        MoodleCourseResponse courseResponse = (listCourse.size() > 0) ? listCourse.get(0) : new MoodleCourseResponse();
        return courseResponse;
    }

    /**
     * get course category detail from moodle
     * @param key : column in mdl_course_categories
     * @param value: value match it
     * @return
     */
    @Override
    public MoodleCourseCategory getCourseCategoryDetailFromMoodleBackend(String key, String value) {
        /* setting resttemplate */
        RestTemplate restTemplate = buildingDefaultResTemplate();
        /* setting headers */
        HttpHeaders headers = buildingDefaultHeaders();
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
        List<MoodleCourseCategory> listCategory = convertJsonToListCourse(response.getBody());
        MoodleCourseCategory moodleCourseCategory = (listCategory.size() > 0) ? listCategory.get(0) : new MoodleCourseCategory();
        return moodleCourseCategory;
    }

    @Override
    public MoodleUploadFile uploadFileInMoodelWithDedicatedEndpoint(ByteArrayResource byteArrayResource, String fileName , String tokenForUser, String userId) {
        RestTemplate restTemplate = buildingDefaultResTemplate();
        HttpHeaders  headers = buildingDefaultHeaders();

        /* keeping param with order by linkedHashMap */
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("token", tokenForUser);
        params.add("component", "user");
        params.add("userid", userId);
        params.add("filearea", "draft");
        params.add("filename", fileName);
        params.add("filepath", "/");
        params.add("licence", "allrightsreserved");
        params.add("author", "");
        params.add("source", "");
        params.add("file_1", convertToHttpEntity(fileName, byteArrayResource.getByteArray()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate
                .postForEntity(moodleServiceDedicatedEnpointUrl, requestEntity, String.class);
        log.info("==== response from moodle backend - upload file to moodle ==== {}", response);
        log.info("==== body from response from moodle backend - upload file to moodle ==== {}", response.getBody());
        if(response.getBody().contains("exception")) {
            ExceptionMoodle exception = DataUtil.jsonToObject(response.getBody(), ExceptionMoodle.class);
            throw new RuntimeException(exception.getMessage());
        }
        List<MoodleUploadFile> result = convertJsonToListUploadFile(response.getBody());
        MoodleUploadFile uploadFile = (result.size() > 0) ? result.get(0) : new MoodleUploadFile();
        return uploadFile;
    }

    @Override
    public MoodleTokenMobile getTokenUserFromMoodle(String username, String password) {
        RestTemplate restTemplate = buildingDefaultResTemplate();
        HttpHeaders  headers = buildingDefaultHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        /* keeping param with order by linkedHashMap */
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", username);
        params.put("password", password);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moodleServiceLoginEndpoint.trim());

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        log.info("=== uri === {}", builder.toUriString());
        HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class, headers);
        log.info("==== response from moodle backend - get token user ==== {}", response);
        log.info("==== body from response from moodle backend - get token user ==== {}", response.getBody());
        if(response.getBody().contains("exception")) {
            ExceptionMoodle exception = DataUtil.jsonToObject(response.getBody(), ExceptionMoodle.class);
            throw new RuntimeException(exception.getMessage());
        }
        MoodleTokenMobile token = DataUtil.jsonToObject(response.getBody(), MoodleTokenMobile.class);
        return token;
    }

    @Override
    public void updateUserEnroll(String userId, String courseId) {
        RestTemplate restTemplate = buildingDefaultResTemplate();
        HttpHeaders  headers = buildingDefaultHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        long startDate = LocalDate.now().toEpochSecond(LocalTime.NOON, ZoneOffset.MIN);
        /* keeping param with order by linkedHashMap */
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("wstoken", moodleServiceUrl.trim());
        params.add("moodlewsrestformat", "json");
        params.add("wsfunction", "enrol_manual_enrol_users");
        params.add("enrolments[0][roleid]", 5);
        params.add("enrolments[0][userid]", Integer.valueOf(userId));
        params.add("enrolments[0][courseid]", Integer.valueOf(courseId));
        params.put("enrolments[0][timestart]", startDate);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moodleServiceUrl.trim());

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(params, headers);
        log.info("=== uri === {}", builder.toUriString());
        ResponseEntity<String> response = restTemplate
                .postForEntity(builder.toUriString(), requestEntity, String.class);

        log.info("==== response from moodle backend - get token user ==== {}", response);
        log.info("==== body from response from moodle backend - get token user ==== {}", response.getBody());
        if(response.getBody().contains("exception")) {
            ExceptionMoodle exception = DataUtil.jsonToObject(response.getBody(), ExceptionMoodle.class);
            if(DataUtil.isNullOrEmpty(exception.getMessage())) {
                throw new RuntimeException(response.getBody());
            }
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void updateUserPicture(String token, String draftItemId, String userId) {
        /* setting resttemplate */
        RestTemplate restTemplate = buildingDefaultResTemplate();
        /* setting headers */
        HttpHeaders headers = buildingDefaultHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        /* keeping param with order by linkedHashMap */
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("wstoken", token);
        params.put("moodlewsrestformat", "json");
        params.put("wsfunction", "core_user_update_picture");
        params.put("draftitemid", draftItemId);
        params.put("userid", userId);


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moodleServiceUrl.trim());

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        log.info("=== uri === {}", builder.toUriString());
        HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class, headers);
        log.info("==== response from moodle backend - update user picture ==== {}", response);
        log.info("==== body from response from moodle backend - update user picture ==== {}", response.getBody());
        if(response.getBody().contains("exception")) {
            ExceptionMoodle exception = DataUtil.jsonToObject(response.getBody(), ExceptionMoodle.class);
            throw new RuntimeException(exception.getMessage());
        }
    }

    /* Utils function*/

    private RestTemplate buildingDefaultResTemplate() {
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        return restTemplate;
    }

    private HttpHeaders buildingDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "application/xml");
        headers.set("Accept-Charset", "utf-8");
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return headers;
    }
    private List<MoodleCourseCategory> convertJsonToListCourse(String jsonArray) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<MoodleCourseCategory>> jacksonTypeReference = new TypeReference<List<MoodleCourseCategory>>() {};
            List<MoodleCourseCategory> categoriyList = objectMapper.readValue(jsonArray, jacksonTypeReference);
            return categoriyList;
        } catch (JsonProcessingException e) {
            log.error("=== error in convertJsonToList - MoodleCourseCategory === {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<MoodleUserResponse> convertJsonToListUserResponse(String jsonArray) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<MoodleUserResponse>> jacksonTypeReference = new TypeReference<List<MoodleUserResponse>>() {};
            List<MoodleUserResponse> result = objectMapper.readValue(jsonArray, jacksonTypeReference);
            return result;
        } catch (JsonProcessingException e) {
            log.error("=== error in convertJsonToList - MoodleCourseCategory === {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<MoodleCourseResponse> convertJsonToListCourseResponse(String jsonArray) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<MoodleCourseResponse>> jacksonTypeReference = new TypeReference<List<MoodleCourseResponse>>() {};
            List<MoodleCourseResponse> result = objectMapper.readValue(jsonArray, jacksonTypeReference);
            return result;
        } catch (JsonProcessingException e) {
            log.error("=== error in convertJsonToList - MoodleCourseCategory === {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<MoodleUploadFile> convertJsonToListUploadFile(String jsonArray) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<MoodleUploadFile>> jacksonTypeReference = new TypeReference<List<MoodleUploadFile>>() {};
            List<MoodleUploadFile> result = objectMapper.readValue(jsonArray, jacksonTypeReference);
            return result;
        } catch (JsonProcessingException e) {
            log.error("=== error in convertJsonToList - MoodleUploadFile === {}", e.getMessage());
            throw new RuntimeException(e);
        }
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

    public static HttpEntity<byte[]> convertToHttpEntity(String filename, byte[] content) {
        ContentDisposition contentDisposition = ContentDisposition.builder("form-data")
                .name("file_1")
                .filename(filename)
                .build();

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        return new HttpEntity<>(content, fileMap);
    }
    /* End */

}
