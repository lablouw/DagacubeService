package com.dagacube.domain.service;

import com.dagacube.domain.model.WagerWinRequest;
import com.dagacube.domain.repository.entity.PlayerTransaction;
import com.dagacube.exception.PlayerExistsException;
import com.dagacube.exception.PlayerInsufficientFundsException;
import com.dagacube.exception.PlayerNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface PlayerService {

	Long createPlayer(String username, BigDecimal balance) throws PlayerExistsException;

	BigDecimal getPlayerBalance(long playerId) throws PlayerNotFoundException;

	PlayerTransaction doWager(WagerWinRequest wagerWinRequest) throws PlayerInsufficientFundsException, PlayerNotFoundException;

	PlayerTransaction doWin(WagerWinRequest wagerWinRequest) throws PlayerNotFoundException;

	List<PlayerTransaction> getPlayerTransactions(String username, int count) throws PlayerNotFoundException;
}
