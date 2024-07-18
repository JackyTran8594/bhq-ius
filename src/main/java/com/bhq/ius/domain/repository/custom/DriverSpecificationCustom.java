package com.bhq.ius.domain.repository.custom;

import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.entity.Profile;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class DriverSpecificationCustom {
    public static Specification<Driver> findAllByCourseId(Long courseId) {
        return (root, query, criteriaBuilder) -> {
            Join<Driver, Course> driverCourseJoin = root.join("course");
//            Join<Driver, Profile> driverProfileJoin = root.join("profile", JoinType.LEFT);
            return criteriaBuilder.equal(driverCourseJoin.get("id"), courseId);
        };
    }
}
