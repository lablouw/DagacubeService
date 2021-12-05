package com.dagacube.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WagerWinRequest {
	private Long playerId;
	private BigDecimal amount;
	private String transactionId;
	private String promoCode;
}
