package com.dagacube.domain.service;

import com.dagacube.domain.model.PlayerTransaction;
import com.dagacube.domain.repository.PlayerRepository;
import com.dagacube.domain.repository.PlayerTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class UserWalletServiceImpl implements UserWalletService {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private PlayerTransactionRepository playerTransactionRepository;

	@Override
	public BigDecimal getUserBalance(int userId) {

	}

	@Override
	public void wager(String playerId, BigDecimal amount, String transactionId) {

	}

	@Override
	public void win(String playerId, BigDecimal amount, String transactionId) {

	}

	@Override
	public List<PlayerTransaction> getPlayerTransactions(String userName, int count) {

	}

}
