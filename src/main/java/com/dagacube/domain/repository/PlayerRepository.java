package com.dagacube.domain.repository;

import com.dagacube.domain.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {



}
