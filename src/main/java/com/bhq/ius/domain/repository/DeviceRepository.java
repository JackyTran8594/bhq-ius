package com.thanhbinh.dms.domain.repository;

import com.thanhbinh.dms.domain.entity.Device;
import com.thanhbinh.dms.domain.repository.custom.DeviceRepositoryCustom;
import com.thanhbinh.dms.domain.repository.specification.DeviceRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>, DeviceRepositorySpecification, DeviceRepositoryCustom {
}
