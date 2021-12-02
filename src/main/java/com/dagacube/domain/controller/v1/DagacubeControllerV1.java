package com.dagacube.domain.controller.v1;

import com.dagacube.api.v1.model.Player;
import com.dagacube.api.v1.model.PlayerTransactionsRequest;
import com.dagacube.domain.repository.entity.PlayerTransaction;
import com.dagacube.domain.service.PlayerService;
import com.dagacube.exception.DagacubeUserAuthorizationException;
import com.dagacube.exception.PlayerInsufficientFundsException;
import com.dagacube.exception.PlayerExistsException;
import com.dagacube.exception.PlayerNotFoundException;
import com.dagacube.exception.TransactionInconsistencyException;
import com.dagacube.mapper.PlayerTransactionMapper;
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

// Service layer should contain only input validation and calls to manager classes.
// No non-domain classes are to be passed into the manager layer, mapping between api and domain classes to take place here.

@RestController
@RequestMapping("/v1/dagacubeService")
@Slf4j
public class DagacubeControllerV1 {

	private static final String PLAYER_HISTORY_USER = "pam";//Specially for Pam
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

		return ResponseEntity.ok().body(playerService.getUserBalance(playerId));

	}

	@PostMapping(value = "/{playerId}/wager/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity wager(@PathVariable long playerId,
								@PathVariable BigDecimal amount,
								@RequestHeader String transactionId) throws PlayerInsufficientFundsException, PlayerNotFoundException,
			TransactionInconsistencyException {

		PlayerTransaction playerTransaction = playerService.wager(playerId, amount, transactionId);

		return ResponseEntity.ok(playerTransaction.getId());
	}

	@PostMapping(value = "/{playerId}/win/{amount}")
	public ResponseEntity win(@PathVariable long playerId,
							  @PathVariable BigDecimal amount,
							  @RequestHeader String transactionId) throws PlayerNotFoundException, TransactionInconsistencyException {

		PlayerTransaction playerTransaction = playerService.win(playerId, amount, transactionId);

		return ResponseEntity.ok(playerTransaction.getId());
	}

	@PostMapping(value = "/{username}/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserTransactions(@RequestBody PlayerTransactionsRequest playerTransactionsRequest) throws PlayerNotFoundException,
			DagacubeUserAuthorizationException {

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

