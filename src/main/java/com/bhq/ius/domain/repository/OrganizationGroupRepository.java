package com.thanhbinh.dms.domain.repository;

import com.thanhbinh.dms.domain.entity.OrganizationGroup;
import com.thanhbinh.dms.domain.repository.custom.OrganizationGroupRepositoryCustom;
import com.thanhbinh.dms.domain.repository.specification.OrganizationGroupRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationGroupRepository extends JpaRepository<OrganizationGroup, Long>, OrganizationGroupRepositorySpecification, OrganizationGroupRepositoryCustom {
}
