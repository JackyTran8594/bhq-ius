package com.bhq.ius.integration.service;

import com.bhq.ius.domain.dto.common.BaseResponseData;

import java.util.List;

public interface IntegrationUserSerive {
    BaseResponseData<?> CreateDrivers(List<Long> listId);
    BaseResponseData<?> CreateCourses(List<Long> listId);
}
