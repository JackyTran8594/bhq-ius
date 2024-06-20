package com.bhq.ius.domain.repository;

import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.repository.custom.DriverRepositoryCustom;
import com.bhq.ius.domain.repository.specification.DriverRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>, DriverRepositorySpecification, DriverRepositoryCustom {
}
