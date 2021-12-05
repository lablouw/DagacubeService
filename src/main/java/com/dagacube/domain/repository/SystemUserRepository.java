package com.dagacube.domain.repository;

import com.dagacube.domain.repository.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

	SystemUser findByUsername(String username);

}
