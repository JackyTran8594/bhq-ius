package com.thanhbinh.dms.domain.repository;

import com.thanhbinh.dms.domain.entity.Organization;
import com.thanhbinh.dms.domain.repository.custom.OrganizationRepositoryCustom;
import com.thanhbinh.dms.domain.repository.specification.OrganizationRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>, OrganizationRepositorySpecification, OrganizationRepositoryCustom {
}
