package com.bhq.ius.domain.repository;

import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.repository.custom.CourseRepositoryCustom;
import com.bhq.ius.domain.repository.specification.CourseRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, CourseRepositorySpecification, CourseRepositoryCustom {
}
