package com.dagacube.domain.controller.v1;

import com.dagacube.api.v1.model.Player;
import com.dagacube.api.v1.model.PlayerTransactionsRequest;
import com.dagacube.api.v1.model.WagerWinRequest;
import com.dagacube.domain.repository.entity.PlayerTransaction;
import com.dagacube.domain.service.PlayerService;
import com.dagacube.exception.PlayerExistsException;
import com.dagacube.exception.PlayerInsufficientFundsException;
import com.dagacube.exception.PlayerNotFoundException;
import com.dagacube.exception.SystemUserAuthorizationException;
import com.dagacube.mapper.PlayerTransactionMapper;
import com.dagacube.mapper.WagerWinRequestMapper;
import com.dagacube.security.SecurityService;
import com.dagacube.util.validation.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

// Controller layer should contain only input validation and calls to service classes.
// No non-domain classes are to be passed into the service layer, mapping between api and domain classes to take place here.

@RestController
@RequestMapping("/v1/dagacubeService")
@Slf4j
public class DagacubeControllerV1 {

	private static final String PLAYER_HISTORY_USER = "pam";//Especially for Pam
	private final PlayerService playerService;
	private final SecurityService securityService;

	public DagacubeControllerV1(PlayerService playerService, SecurityService securityService) {
		this.playerService = playerService;
		this.securityService = securityService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createPlayer(@RequestBody Player player) throws PlayerExistsException {
		ResponseEntity<List<String>> validationResult = ValidationUtil.validate(player);
		if (validationResult != null) {
			return validationResult;
		}

		Long playerId = playerService.createPlayer(player.getUsername(), player.getBalance());

		return ResponseEntity.status(HttpStatus.CREATED).body(playerId);
	}

	@GetMapping(value = "/{playerId}/balance")
	public ResponseEntity<BigDecimal> getUserBalance(@PathVariable int playerId) throws PlayerNotFoundException {

		return ResponseEntity.ok().body(playerService.getPlayerBalance(playerId));

	}

	@PostMapping(value = "/wager", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity wager(@RequestBody WagerWinRequest wagerWinRequest, @RequestHeader String transactionId) throws
			PlayerInsufficientFundsException, PlayerNotFoundException {
		ResponseEntity<List<String>> validationResult = ValidationUtil.validate(wagerWinRequest);
		if (validationResult != null) {
			return validationResult;
		}

		com.dagacube.domain.model.WagerWinRequest wwr = WagerWinRequestMapper.INSTANCE.mapToModel(wagerWinRequest, transactionId);

		PlayerTransaction playerTransaction = playerService.doWager(wwr);

		return ResponseEntity.ok(playerTransaction.getId());
	}

	@PostMapping(value = "/win", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity win(@RequestBody WagerWinRequest wagerWinRequest, @RequestHeader String transactionId) throws PlayerNotFoundException {
		ResponseEntity<List<String>> validationResult = ValidationUtil.validate(wagerWinRequest);
		if (validationResult != null) {
			return validationResult;
		}

		com.dagacube.domain.model.WagerWinRequest wwr = WagerWinRequestMapper.INSTANCE.mapToModel(wagerWinRequest, transactionId);

		PlayerTransaction playerTransaction = playerService.doWin(wwr);

		return ResponseEntity.ok(playerTransaction.getId());
	}

	@PostMapping(value = "/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserTransactions(@RequestBody PlayerTransactionsRequest playerTransactionsRequest) throws PlayerNotFoundException,
			SystemUserAuthorizationException {

		securityService.verifyPassword(PLAYER_HISTORY_USER, playerTransactionsRequest.getPassword());

		ResponseEntity<List<String>> validationResult = ValidationUtil.validate(playerTransactionsRequest);
		if (validationResult != null) {
			return validationResult;
		}

		List<PlayerTransaction> playerTransactions = playerService.getPlayerTransactions(playerTransactionsRequest.getUsername(),
				playerTransactionsRequest.getCount() == null ? 10 : playerTransactionsRequest.getCount());

		return ResponseEntity.ok().body(PlayerTransactionMapper.INSTANCE.mapToV1(playerTransactions));

	}

}

