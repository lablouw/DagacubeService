package com.dagacube.exception.handler;

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
}
