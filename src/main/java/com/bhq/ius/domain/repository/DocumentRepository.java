package com.bhq.ius.domain.repository;

import com.bhq.ius.domain.entity.Document;
import com.bhq.ius.domain.repository.custom.DocumentRepositoryCustom;
import com.bhq.ius.domain.repository.specification.DocumentRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, DocumentRepositorySpecification, DocumentRepositoryCustom {
}
