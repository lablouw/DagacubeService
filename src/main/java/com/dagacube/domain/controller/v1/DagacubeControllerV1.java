package com.dagacube.domain.controller.v1;

import com.dagacube.api.v1.model.UserTransactionsRequest;
import com.dagacube.domain.model.PlayerTransaction;
import com.dagacube.domain.service.UserWalletService;
import com.dagacube.mapper.PlayerTransactionMapper;
import com.dagacube.util.validation.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
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

	private final UserWalletService userWalletService;

	public DagacubeControllerV1(UserWalletService userWalletService) {
		this.userWalletService = userWalletService;
	}

	@GetMapping(value = "/{playerId}/balance")
	public ResponseEntity<BigDecimal> getUserBalance(@PathVariable int userId) {

		return ResponseEntity.ok().body(userWalletService.getUserBalance(userId));

	}

	@PostMapping(value = "/{playerId}/wager/{amount}")
	public ResponseEntity wager(@PathVariable String playerId,
								@PathVariable BigDecimal amount,
								@RequestHeader String transactionId) {

		userWalletService.wager(playerId, amount, transactionId);

		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/{playerId}/win/{amount}")
	public ResponseEntity win(@PathVariable String playerId,
							  @PathVariable BigDecimal amount,
							  @RequestHeader String transactionId) {

		userWalletService.win(playerId, amount, transactionId);

		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/{username}/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserTransactions(@RequestBody UserTransactionsRequest userTransactionsRequest) {

		ResponseEntity<List<String>> validationResult = ValidationUtil.validate(userTransactionsRequest);
		if (validationResult != null) {
			return validationResult;
		}

		List<PlayerTransaction> playerTransactions = userWalletService.getPlayerTransactions(userTransactionsRequest.getUserName(),
				userTransactionsRequest.getCount() == null ? 10 : userTransactionsRequest.getCount());



		return ResponseEntity.ok().body(PlayerTransactionMapper.INSTANCE.mapToV1(playerTransactions));

	}

}

