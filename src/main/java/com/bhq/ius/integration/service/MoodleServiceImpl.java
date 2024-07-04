package com.bhq.ius.integration.service;

import com.bhq.ius.domain.entity.Profile;
import com.bhq.ius.integration.dto.ExceptionMoodle;
import com.bhq.ius.integration.dto.MoodleCourse;
import com.bhq.ius.integration.dto.MoodleCourseCategory;
import com.bhq.ius.integration.dto.MoodleUser;
import com.bhq.ius.utils.DataUtil;
import com.bhq.ius.utils.XmlUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Override
    public void postUserToMoodleBackend(MoodleUser user) {
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
    }

    @Override
    public void postCourseToMoodleBackend(MoodleCourse data) {
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
        List<MoodleCourseCategory> listCategory = convertJsonToList(response.getBody());
        MoodleCourseCategory moodleCourseCategory = (listCategory.size() > 0) ? listCategory.get(0) : new MoodleCourseCategory();
        return moodleCourseCategory;
    }

    @Override
    public String uploadFileInMoodelWithDedicatedEndpoint(Profile profile, String tokenForUser) {
        return null;
    }

    @Override
    public String getTokenUserFromMoodle(String username, String password) {
        return null;
    }

    @Override
    public void updateUserPicture(String token, String draftItemId) {

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
    private List<MoodleCourseCategory> convertJsonToList(String jsonArray) {
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
    /* End */

}
