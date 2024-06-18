package com.bhq.ius.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.bhq.ius.utils.DataUtil.camelToSnake;

@Component
public class CommonUtil {

    @SuppressWarnings("java:S3776")
    public static Sort sort(List<String> sort) {
        if (CollectionUtils.isEmpty(sort)) {
            return null;
        }

        List<Sort.Order> orderList = new ArrayList<>();
        if (sort.get(0).contains(",")) {
            String[] strArray;
            for (String str : sort) {
                strArray = str.split(",");
                if (strArray.length > 1) {
                    if ("asc".equalsIgnoreCase(strArray[0])) {
                        orderList.add(Sort.Order.asc(camelToSnake(strArray[0])));
                    } else {
                        orderList.add(Sort.Order.desc(camelToSnake(strArray[0])));
                    }
                } else {
                    orderList.add(Sort.Order.asc(camelToSnake(strArray[0])));
                }
            }
        } else {
            for(String s: sort) {
                orderList.add(Sort.Order.asc(camelToSnake(s)));
            }
        }
        return Sort.by(orderList);
    }
    public static PageRequest pageRequest(List<String> sort, Integer page, Integer size) {
        if(CollectionUtils.isEmpty(sort)) {
            return PageRequest.of(page, size);
        }
        return PageRequest.of(page, size, sort(sort));
    }

}
