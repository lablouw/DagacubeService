package com.dagacube.domain.repository;

import com.dagacube.domain.repository.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

	Optional<Player> findByUsername(String userName);

}
