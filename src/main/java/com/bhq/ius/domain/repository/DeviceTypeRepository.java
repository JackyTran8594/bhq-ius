package com.thanhbinh.dms.domain.repository;

import com.thanhbinh.dms.domain.entity.DeviceType;
import com.thanhbinh.dms.domain.repository.custom.DeviceTypeRepositoryCustom;
import com.thanhbinh.dms.domain.repository.specification.DeviceTypeRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTypeRepository extends JpaRepository<DeviceType, Long>, DeviceTypeRepositorySpecification, DeviceTypeRepositoryCustom {
}
