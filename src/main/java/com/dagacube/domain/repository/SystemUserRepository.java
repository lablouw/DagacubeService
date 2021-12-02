package com.dagacube.domain.repository;

import com.dagacube.domain.repository.entity.DagacubeSystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemUserRepository extends JpaRepository<DagacubeSystemUser, Long> {

	DagacubeSystemUser findByUsername(String username);

}
