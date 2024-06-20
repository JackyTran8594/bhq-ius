package com.bhq.ius.domain.repository;

import com.bhq.ius.domain.entity.Profile;
import com.bhq.ius.domain.entity.User;
import com.bhq.ius.domain.repository.custom.ProfileRepositoryCustom;
import com.bhq.ius.domain.repository.specification.ProfileRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileRepositorySpecification, ProfileRepositoryCustom {
}
