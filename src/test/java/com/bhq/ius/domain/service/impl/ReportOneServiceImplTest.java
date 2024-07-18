package com.bhq.ius.domain.service.impl;

import com.bhq.ius.constant.RecordState;
import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.repository.DriverRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@Slf4j
class ReportOneServiceImplTest {

    @InjectMocks
    ReportOneServiceImpl service;

    @Mock
    DriverRepository repository;

    @Test
    void givenListIdEmpty_WhenSubmitDriver_ShouldReturnAllDriverWithStateNullAndStateNotIgnoreEqualCase() {
        List<Driver> driverList = new ArrayList<>();
        Driver driver1 = new Driver();
        driver1.setState(RecordState.SUBMITTED);
        driver1.setHoVaTen("Test");

        Driver driver2 = new Driver();
        driver2.setHoVaTen("Test2");

        driverList.add(driver1);
        driverList.add(driver2);
        when(repository.findAllByStateNullOrStateNotIn(any())).thenReturn(Collections.singletonList(driver2));

        assertEquals(1, service.getListDriver(null, null).size());
        assertEquals("Test2", service.getListDriver(null, null).get(0).getHoVaTen());

    }


}