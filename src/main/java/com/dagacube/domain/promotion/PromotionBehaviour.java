package com.dagacube.domain.promotion;

import com.dagacube.domain.model.WagerWinRequest;
import com.dagacube.domain.repository.entity.Player;
import com.dagacube.domain.repository.entity.Promotion;

public interface PromotionBehaviour {

	void preWager(WagerWinRequest wagerWinRequest, Player player, Promotion promotion);
	void postWager(WagerWinRequest wagerWinRequest, Player player, Promotion promotion);

	void preWin(WagerWinRequest wagerWinRequest, Player player, Promotion promotion);
	void postWin(WagerWinRequest wagerWinRequest, Player player, Promotion promotion);


}
