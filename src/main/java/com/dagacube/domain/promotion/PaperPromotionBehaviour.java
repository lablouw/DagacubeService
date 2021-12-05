package com.dagacube.domain.promotion;

import com.dagacube.domain.model.WagerWinRequest;
import com.dagacube.domain.repository.PlayerPromotionRepository;
import com.dagacube.domain.repository.entity.Player;
import com.dagacube.domain.repository.entity.PlayerPromotion;
import com.dagacube.domain.repository.entity.Promotion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Slf4j
public class PaperPromotionBehaviour implements PromotionBehaviour {

	@Autowired
	private PlayerPromotionRepository playerPromotionRepository;

	@Override
	public void preWager(WagerWinRequest wagerWinRequest, Player player, Promotion promotion) {


		PlayerPromotion playerPromotion = playerPromotionRepository.getByPlayerIdAndPromotionId(wagerWinRequest.getPlayerId(), promotion.getId());

		if (playerPromotion == null) {
			playerPromotion = PlayerPromotion.builder()
					.promotion(promotion)
					.player(player)
					.value(promotion.getDataValue())
					.build();
		}

		if (promotion.getCode().equals(wagerWinRequest.getPromoCode())) {
			//code provided, reset player's free wager count
			playerPromotion.setValue(promotion.getDataValue());
		} else if (Integer.parseInt(playerPromotion.getValue()) <= 0) {
			return;
		}
		wagerWinRequest.setAmount(BigDecimal.ZERO);
		playerPromotion.setValue(String.valueOf(Integer.parseInt(playerPromotion.getValue()) - 1));

		log.info("Executed promotion. [promotionid={}, playerId={}, transactionId={}]", promotion.getId(), player.getId(), wagerWinRequest.getTransactionId());

		playerPromotionRepository.save(playerPromotion);
	}

	@Override
	public void postWager(WagerWinRequest wagerWinRequest, Player player, Promotion promotion) {
		//nothing to do
	}

	@Override
	public void preWin(WagerWinRequest wagerWinRequest, Player player, Promotion promotion) {
		//nothing to do
	}

	@Override
	public void postWin(WagerWinRequest wagerWinRequest, Player player, Promotion promotion) {
		//nothing to do
	}
}
