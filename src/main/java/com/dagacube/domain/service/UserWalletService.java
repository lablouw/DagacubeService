package com.dagacube.domain.service;

import com.dagacube.domain.model.PlayerTransaction;

import java.math.BigDecimal;
import java.util.List;

public interface UserWalletService {

	BigDecimal getUserBalance(int userId);

	void wager(String playerId, BigDecimal amount, String transactionId);

	void win(String playerId, BigDecimal amount, String transactionId);

	List<PlayerTransaction> getPlayerTransactions(String userName, int count);
}
