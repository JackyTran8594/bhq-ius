package com.bhq.ius.utils;

import com.bhq.ius.constant.IusConstant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
class DataUtilTest {

    @InjectMocks
    DataUtil dataUtil;

    @Test
    void convertStringWithFormatDate() {
        String date = "19830327";
        String result = "1983-03-27";
        System.out.println("==== date ===== " + dataUtil.convertDateOfBirthWithFormat(date));
        assertEquals(result, dataUtil.convertDateOfBirthWithFormat(date));
    }

    @Test
    void convertStringToLocalDate() {
        String date = "1983-03-27";
        LocalDate result = LocalDate.of(1983,03,27);
        System.out.println("==== date ===== " + dataUtil.convertStringToLocalDate(date, "yyyy-MM-dd"));
        assertEquals(result, dataUtil.convertStringToLocalDate(date, "yyyy-MM-dd"));
    }

    @Test
    void convertLocalDateToString() {
        LocalDate date = LocalDate.of(1983,03,27);
        String expect = "19830327";
        System.out.println("==== date ===== " + dataUtil.convertLocalDateToString(date));
        assertEquals(expect, dataUtil.convertLocalDateToString(date));

    }
}