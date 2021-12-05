package com.dagacube.exception.handler;

import com.dagacube.exception.PlayerExistsException;
import com.dagacube.exception.PlayerInsufficientFundsException;
import com.dagacube.exception.PlayerNotFoundException;
import com.dagacube.exception.SystemUserAuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class DagacubeServiceExceptionHandler extends ResponseEntityExceptionHandler {

	@Value("${uncaught.rest.exception.ticket.generation:true}")
	private boolean generateTicketErrors;

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<String> handleUncaughtExceptions(Exception ex) throws Exception {
		if (generateTicketErrors) {
			String ticketError = Long.toHexString(System.currentTimeMillis());
			log.error("Uncaught internal fault. Ticket error returned to caller [ticketError={}]", ticketError, ex);
			return new ResponseEntity<>("Internal server error. Ticket=" + ticketError, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		throw ex;
	}

	@ExceptionHandler(PlayerInsufficientFundsException.class)
	public final ResponseEntity handlePlayerOutOfFundsException(PlayerInsufficientFundsException ex) {
		return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Player is broke, give 'em some tea!");
	}

	@ExceptionHandler(PlayerNotFoundException.class)
	public final ResponseEntity handlePlayerNotFoundException(PlayerNotFoundException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(PlayerExistsException.class)
	public final ResponseEntity handlePlayerExistsException(PlayerExistsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(SystemUserAuthorizationException.class)
	public final ResponseEntity handleDagacubeUserAuthorizationException(SystemUserAuthorizationException ex) {
		return new ResponseEntity(HttpStatus.UNAUTHORIZED);
	}

}
