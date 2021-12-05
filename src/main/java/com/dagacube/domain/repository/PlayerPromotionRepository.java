package com.dagacube.domain.repository;

import com.dagacube.domain.repository.entity.PlayerPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerPromotionRepository extends JpaRepository<PlayerPromotion, Long> {

	PlayerPromotion getByPlayerIdAndPromotionId(Long playerId, long id);

	List<PlayerPromotion> findAllByPlayerId(long id);
}
