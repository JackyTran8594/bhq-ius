package com.thanhbinh.dms.domain.repository;

import com.thanhbinh.dms.domain.entity.DeviceGroup;
import com.thanhbinh.dms.domain.repository.custom.DeviceGroupRepositoryCustom;
import com.thanhbinh.dms.domain.repository.specification.DeviceGroupRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long>, DeviceGroupRepositorySpecification, DeviceGroupRepositoryCustom {
}
