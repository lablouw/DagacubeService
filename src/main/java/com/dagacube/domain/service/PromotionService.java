package com.dagacube.domain.service;

import com.dagacube.domain.model.WagerWinRequest;
import com.dagacube.domain.repository.entity.Player;

public interface PromotionService {

	void executePreWagerPromotionBehaviours(WagerWinRequest wagerWinRequest, Player player) throws ClassNotFoundException;

	void executePostWagerPromotionBehaviours(WagerWinRequest wagerWinRequest, Player player) throws ClassNotFoundException;

	void executePreWinPromotionBehaviours(WagerWinRequest wagerWinRequest, Player player) throws ClassNotFoundException;

	void executePostWinPromotionBehaviours(WagerWinRequest wagerWinRequest, Player player) throws ClassNotFoundException;

}
