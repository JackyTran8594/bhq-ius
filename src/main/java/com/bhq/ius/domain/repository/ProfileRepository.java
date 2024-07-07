package com.bhq.ius.domain.repository;

import com.bhq.ius.constant.RecordState;
import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.entity.Profile;
import com.bhq.ius.domain.entity.User;
import com.bhq.ius.domain.repository.custom.ProfileRepositoryCustom;
import com.bhq.ius.domain.repository.specification.ProfileRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileRepositorySpecification, ProfileRepositoryCustom {
    List<Profile> findAllByStateNullOrStateNotIn(List<RecordState> state);
}
