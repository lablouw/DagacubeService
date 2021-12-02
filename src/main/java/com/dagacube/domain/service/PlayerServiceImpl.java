package com.dagacube.domain.service;

import com.dagacube.domain.types.TransactionType;
import com.dagacube.domain.repository.PlayerRepository;
import com.dagacube.domain.repository.PlayerTransactionRepository;
import com.dagacube.domain.repository.entity.Player;
import com.dagacube.domain.repository.entity.PlayerTransaction;
import com.dagacube.exception.PlayerExistsException;
import com.dagacube.exception.PlayerNotFoundException;
import com.dagacube.exception.PlayerInsufficientFundsException;
import com.dagacube.exception.TransactionInconsistencyException;
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
	public PlayerTransaction wager(long playerId, BigDecimal amount, String transactionId) throws PlayerInsufficientFundsException, PlayerNotFoundException,
			TransactionInconsistencyException {

		//First order of business: idempotency
		PlayerTransaction playerTransaction = playerTransactionRepository.findByTransactionId(transactionId).orElse(null);
		if (playerTransaction != null) {
			if (amount.compareTo(playerTransaction.getAmount()) != 0 || playerId != playerTransaction.getPlayer().getId() || !playerTransaction.getTransactionType().equals(TransactionType.WAGER)) {
				throw new TransactionInconsistencyException("playerId/amount/type inconsistent for given transactionId");
			}
			else {
				return playerTransaction;
			}
		}

		Player player = getPlayerById(playerId);

		//Check if player has enough funds
		if (player.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
			throw new PlayerInsufficientFundsException();
		}

		//Create new transaction
		BigDecimal newBalance = player.getBalance().subtract(amount);
		playerTransaction = PlayerTransaction.builder()
				.player(player)
				.transactionId(transactionId)
				.transactionType(TransactionType.WAGER)
				.created(new Date())
				.amount(amount)
				.postBalance(newBalance).build();
		playerTransactionRepository.save(playerTransaction);

		player.setBalance(newBalance);
		playerRepository.save(player);

		return playerTransaction;

	}

	@Override
	@Transactional
	public PlayerTransaction win(long playerId, BigDecimal amount, String transactionId) throws PlayerNotFoundException,
			TransactionInconsistencyException {
		//First order of business: idempotency
		PlayerTransaction playerTransaction = playerTransactionRepository.findByTransactionId(transactionId).orElse(null);
		if (playerTransaction != null) {
			if (amount.compareTo(playerTransaction.getAmount()) != 0 || playerId != playerTransaction.getPlayer().getId() || !playerTransaction.getTransactionType().equals(TransactionType.WIN)) {
				throw new TransactionInconsistencyException("playerId/amount/type inconsistent for given transactionId");
			}
			else {
				return playerTransaction;
			}
		}

		Player player = getPlayerById(playerId);

		//Create new transaction
		BigDecimal newBalance = player.getBalance().add(amount);
		playerTransaction = PlayerTransaction.builder()
				.player(player)
				.transactionId(transactionId)
				.transactionType(TransactionType.WIN)
				.created(new Date())
				.amount(amount)
				.postBalance(newBalance).build();
		playerTransactionRepository.save(playerTransaction);

		player.setBalance(newBalance);
		playerRepository.save(player);

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
