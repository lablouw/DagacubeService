package com.dagacube.domain.service;

import com.dagacube.domain.repository.entity.PlayerTransaction;
import com.dagacube.exception.PlayerExistsException;
import com.dagacube.exception.PlayerInsufficientFundsException;
import com.dagacube.exception.PlayerNotFoundException;
import com.dagacube.exception.TransactionInconsistencyException;

import java.math.BigDecimal;
import java.util.List;

public interface PlayerService {

	Long createPlayer(String username, BigDecimal balance) throws PlayerExistsException;

	BigDecimal getUserBalance(long playerId) throws PlayerNotFoundException;

	PlayerTransaction wager(long playerId, BigDecimal amount, String transactionId) throws PlayerInsufficientFundsException, PlayerNotFoundException,
			TransactionInconsistencyException;

	PlayerTransaction win(long playerId, BigDecimal amount, String transactionId) throws PlayerNotFoundException, TransactionInconsistencyException;

	List<PlayerTransaction> getPlayerTransactions(String username, int count) throws PlayerNotFoundException;
}
