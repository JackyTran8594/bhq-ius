package com.bhq.ius.domain.repository.custom;

import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.entity.Driver;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class DriverSpecificationCustom {
    public static Specification<Driver> findAllByCourseId(Long courseId) {
        return (root, query, criteriaBuilder) -> {
            Join<Driver, Course> driverCourseJoin = root.join("course");
            return criteriaBuilder.equal(driverCourseJoin.get("id"), courseId);
        };
    }
}
