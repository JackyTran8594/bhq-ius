package com.bhq.ius.integration.service;

import com.bhq.ius.domain.dto.common.BaseResponseData;
import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.entity.Driver;

import java.util.List;

public interface IntegrationUserSerive {
    List<Long> CreateDrivers(List<Driver> listDriver);
    List<Long> CreateCourses(List<Course> listCourse);
    List<Long> UpdateUserPicture(List<Driver> listDriver);
}
