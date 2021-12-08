package com.dagacube.domain.service;

import com.dagacube.domain.model.WagerWinRequest;
import com.dagacube.domain.promotion.PromotionBehaviour;
import com.dagacube.domain.repository.PlayerPromotionRepository;
import com.dagacube.domain.repository.PromotionRepository;
import com.dagacube.domain.repository.entity.Player;
import com.dagacube.domain.repository.entity.PlayerPromotion;
import com.dagacube.domain.repository.entity.Promotion;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private PromotionRepository promotionRepository;

	@Autowired
	private PlayerPromotionRepository playerPromotionRepository;

	private final Map<String, PromotionBehaviour> promotionBehaviourMap = new HashMap<>();

	@Override
	public void executePreWagerPromotionBehaviours(WagerWinRequest wagerWinRequest, Player player) throws ClassNotFoundException {

		Long executedPromotionId = null;
		//Execute the promotion provided
		if (!StringUtils.isEmpty(wagerWinRequest.getPromoCode())) {
			Promotion promotion = promotionRepository.getByCode(wagerWinRequest.getPromoCode());
			PromotionBehaviour promotionBehaviour = getByFqcn(promotion.getBehaviourFqcn());
			promotionBehaviour.preWager(wagerWinRequest, player, promotion);
			executedPromotionId = promotion.getId();
		}

		//Execute any other promotions the player may have
		List<PlayerPromotion> playerPromotions = playerPromotionRepository.findAllByPlayerId(player.getId());
		for (PlayerPromotion playerPromotion : playerPromotions) {
			Promotion promotion = playerPromotion.getPromotion();
			if (executedPromotionId == null || promotion.getId() != executedPromotionId) {
				PromotionBehaviour promotionBehaviour = getByFqcn(promotion.getBehaviourFqcn());
				promotionBehaviour.preWager(wagerWinRequest, player, promotion);
			}
		}

	}

	@Override
	public void executePostWagerPromotionBehaviours(WagerWinRequest wagerWinRequest, Player player) throws ClassNotFoundException {

		Long executedPromotionId = null;
		//Execute the promotion provided
		if (!StringUtils.isEmpty(wagerWinRequest.getPromoCode())) {
			Promotion promotion = promotionRepository.getByCode(wagerWinRequest.getPromoCode());
			PromotionBehaviour promotionBehaviour = getByFqcn(promotion.getBehaviourFqcn());
			promotionBehaviour.postWager(wagerWinRequest, player, promotion);
			executedPromotionId = promotion.getId();
		}

		//Execute any other promotions the player may have
		List<PlayerPromotion> playerPromotions = playerPromotionRepository.findAllByPlayerId(player.getId());
		for (PlayerPromotion playerPromotion : playerPromotions) {
			Promotion promotion = playerPromotion.getPromotion();
			if (executedPromotionId == null || promotion.getId() != executedPromotionId) {
				PromotionBehaviour promotionBehaviour = getByFqcn(promotion.getBehaviourFqcn());
				promotionBehaviour.postWager(wagerWinRequest, player, promotion);
			}
		}

	}

	@Override
	public void executePreWinPromotionBehaviours(WagerWinRequest wagerWinRequest, Player player) throws ClassNotFoundException {

		Long executedPromotionId = null;
		//Execute the promotion provided
		if (!StringUtils.isEmpty(wagerWinRequest.getPromoCode())) {
			Promotion promotion = promotionRepository.getByCode(wagerWinRequest.getPromoCode());
			PromotionBehaviour promotionBehaviour = getByFqcn(promotion.getBehaviourFqcn());
			promotionBehaviour.preWin(wagerWinRequest, player, promotion);
			executedPromotionId = promotion.getId();
		}

		//Execute any other promotions the player may have
		List<PlayerPromotion> playerPromotions = playerPromotionRepository.findAllByPlayerId(player.getId());
		for (PlayerPromotion playerPromotion : playerPromotions) {
			Promotion promotion = playerPromotion.getPromotion();
			if (executedPromotionId == null || promotion.getId() != executedPromotionId) {
				PromotionBehaviour promotionBehaviour = getByFqcn(promotion.getBehaviourFqcn());
				promotionBehaviour.preWin(wagerWinRequest, player, promotion);
			}
		}

	}

	@Override
	public void executePostWinPromotionBehaviours(WagerWinRequest wagerWinRequest, Player player) throws ClassNotFoundException {

		Long executedPromotionId = null;
		//Execute the promotion provided
		if (!StringUtils.isEmpty(wagerWinRequest.getPromoCode())) {
			Promotion promotion = promotionRepository.getByCode(wagerWinRequest.getPromoCode());
			PromotionBehaviour promotionBehaviour = getByFqcn(promotion.getBehaviourFqcn());
			promotionBehaviour.postWin(wagerWinRequest, player, promotion);
			executedPromotionId = promotion.getId();
		}

		//Execute any other promotions the player may have
		List<PlayerPromotion> playerPromotions = playerPromotionRepository.findAllByPlayerId(player.getId());
		for (PlayerPromotion playerPromotion : playerPromotions) {
			Promotion promotion = playerPromotion.getPromotion();
			if (executedPromotionId == null || promotion.getId() != executedPromotionId) {
				PromotionBehaviour promotionBehaviour = getByFqcn(promotion.getBehaviourFqcn());
				promotionBehaviour.postWin(wagerWinRequest, player, promotion);
			}
		}

	}

	private synchronized PromotionBehaviour getByFqcn(String fqcn) throws ClassNotFoundException {
		PromotionBehaviour promotionBehaviour = promotionBehaviourMap.get(fqcn);
		if (promotionBehaviour == null) {
			try {
				promotionBehaviour = (PromotionBehaviour) applicationContext.getBean(Class.forName(fqcn));
				promotionBehaviourMap.put(fqcn, promotionBehaviour);
			} catch (NoSuchBeanDefinitionException e) {
				promotionBehaviour = (PromotionBehaviour) applicationContext.getAutowireCapableBeanFactory().createBean(Class.forName(fqcn));
				promotionBehaviourMap.put(fqcn, promotionBehaviour);
			}
		}

		return promotionBehaviour;
	}
}
