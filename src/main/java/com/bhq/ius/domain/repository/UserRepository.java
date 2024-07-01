package com.bhq.ius.domain.repository;

import com.bhq.ius.domain.entity.User;
import com.bhq.ius.domain.repository.custom.UserRepositoryCustom;
import com.bhq.ius.domain.repository.specification.UserRepositorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositorySpecification, UserRepositoryCustom {

    User findByUsername(String username);
}
