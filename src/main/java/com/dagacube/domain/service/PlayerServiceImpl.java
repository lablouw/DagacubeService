package com.dagacube.domain.service;

import com.dagacube.domain.model.WagerWinRequest;
import com.dagacube.domain.types.TransactionType;
import com.dagacube.domain.repository.PlayerRepository;
import com.dagacube.domain.repository.PlayerTransactionRepository;
import com.dagacube.domain.repository.entity.Player;
import com.dagacube.domain.repository.entity.PlayerTransaction;
import com.dagacube.exception.PlayerExistsException;
import com.dagacube.exception.PlayerNotFoundException;
import com.dagacube.exception.PlayerInsufficientFundsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private PlayerTransactionRepository playerTransactionRepository;

	@Autowired
	private PromotionService promotionService;

	@Override
	public Long createPlayer(String username, BigDecimal balance) throws PlayerExistsException {
		try {
			return playerRepository.save(Player.builder().username(username).balance(balance).build()).getId();
		} catch (DataIntegrityViolationException ex) {
			throw new PlayerExistsException("Player exists. username=" + username);
		}
	}

	@Override
	@Transactional
	public BigDecimal getUserBalance(long playerId) throws PlayerNotFoundException {
		return getPlayerById(playerId).getBalance();
	}

	@Override
	@Transactional
	public PlayerTransaction doWager(WagerWinRequest wager) throws PlayerInsufficientFundsException, PlayerNotFoundException {

		//First order of business: idempotency
		PlayerTransaction playerTransaction = playerTransactionRepository.findByTransactionId(wager.getTransactionId()).orElse(null);
		if (playerTransaction != null) {
			return playerTransaction;
		}

		Player player = getPlayerById(wager.getPlayerId());

		//Business question: if the promotion fails, do we continue with the rest of the transaction. implemented = yes.
		boolean promotionPreBehaviourSuccess = true;
		try {
			promotionService.executePreWagerPromotionBehaviours(wager, player);
		} catch (Exception ex) {
			log.error("Pre-wager promotion behaviour failed. Continuing. [winWagerRequest={}]", wager, ex);
			promotionPreBehaviourSuccess = false;
		}


		//Check if player has enough funds
		if (player.getBalance().subtract(wager.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
			throw new PlayerInsufficientFundsException();
		}

		//Create new transaction
		BigDecimal newBalance = player.getBalance().subtract(wager.getAmount());
		playerTransaction = PlayerTransaction.builder()
				.player(player)
				.transactionId(wager.getTransactionId())
				.transactionType(TransactionType.WAGER)
				.created(new Date())
				.amount(wager.getAmount())
				.postBalance(newBalance).build();
		playerTransactionRepository.save(playerTransaction);

		player.setBalance(newBalance);
		playerRepository.save(player);

		//Business question: If the post behaviour fails, do we throw an ex and rollback everything? Implemented = no.
		if (promotionPreBehaviourSuccess) {
			try {
				promotionService.executePostWagerPromotionBehaviours(wager, player);
			} catch (Exception ex) {
				log.error("Post-wager promotion behaviour failed. Continuing. [winWagerRequest={}]", wager, ex);
			}
		}

		return playerTransaction;

	}

	@Override
	@Transactional
	public PlayerTransaction doWin(WagerWinRequest win) throws PlayerNotFoundException {
		//First order of business: idempotency
		PlayerTransaction playerTransaction = playerTransactionRepository.findByTransactionId(win.getTransactionId()).orElse(null);
		if (playerTransaction != null) {
			return playerTransaction;
		}

		Player player = getPlayerById(win.getPlayerId());

		//Business requirement: if the pre behaviour fails, do we continue with the rest of the transaction. implemented = yes.
		try {
			promotionService.executePreWinPromotionBehaviours(win, player);
		} catch (Exception ex) {
			log.error("Pre-win promotion behaviour failed. Continuing. [winWagerRequest={}]", win, ex);
		}

		//Create new transaction
		BigDecimal newBalance = player.getBalance().add(win.getAmount());
		playerTransaction = PlayerTransaction.builder()
				.player(player)
				.transactionId(win.getTransactionId())
				.transactionType(TransactionType.WIN)
				.created(new Date())
				.amount(win.getAmount())
				.postBalance(newBalance).build();
		playerTransactionRepository.save(playerTransaction);

		player.setBalance(newBalance);
		playerRepository.save(player);

		//Business requirement: If the post behaviour fails, do we throw an ex and rollback everything? Implemented = no.
		try {
			promotionService.executePostWinPromotionBehaviours(win, player);
		} catch (Exception ex) {
			log.error("Pre-win promotion behaviour failed. Continuing. [winWagerRequest={}]", win, ex);
		}

		return playerTransaction;
	}

	@Override
	@Transactional
	public List<PlayerTransaction> getPlayerTransactions(String username, int count) throws PlayerNotFoundException {
		
		Player player = getPlayerByUsername(username);
		
		return playerTransactionRepository.findAllByPlayerIdOrderByCreatedDesc(player.getId(), Pageable.ofSize(count));
	}

	private Player getPlayerById(long playerId) throws PlayerNotFoundException {
		Optional<Player> player = playerRepository.findById(playerId);
		if (player.isPresent()) {
			return player.get();
		} else {
			throw new PlayerNotFoundException("Player not found. playerId=" + playerId);
		}
	}

	private Player getPlayerByUsername(String username) throws PlayerNotFoundException {
		Optional<Player> player = playerRepository.findByUsername(username);
		if (player.isPresent()) {
			return player.get();
		} else {
			throw new PlayerNotFoundException("Player not found. username=" + username);
		}
	}
	
}
