package com.bhq.ius.domain.repository;

import com.bhq.ius.constant.RecordState;
import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.repository.custom.DriverRepositoryCustom;
import com.bhq.ius.domain.repository.specification.DriverRepositorySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>, DriverRepositorySpecification, DriverRepositoryCustom {

    @Query(value = "SELECT d.* FROM driver AS d WHERE 1=1 AND d.course_id = :courseId AND d.state NOT IN ('SUBMITTED') OR d.state IS NULL", nativeQuery = true)
    List<Driver> findAllByStateNullOrStateNotIn(@Param("courseId") Long courseId);
    @Query(value = "SELECT d.* FROM driver AS d LEFT JOIN profile AS p ON d.uuid = p.driver_uuid WHERE 1=1 AND d.course_id = :courseId AND p.state NOT IN ('SUBMITTED') OR p.state IS NULL", nativeQuery = true)
    List<Driver> findAllByStateNullOrStateNotInWithProfile(@Param("courseId") Long courseId);
    List<Driver> findAllByStateEnrollNullOrStateEnrollNotInAndCourse(List<RecordState> recordState, Course course);
    Long countByStateIn(List<RecordState> recordState);
    Long countByStateEnrollIn(List<RecordState> recordState);

}
