package com.dagacube.api.v1.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel
public class PlayerTransaction {
	private Long id;
	private Date created;
	private String transactionType;
	private BigDecimal amount;
	private BigDecimal postBalance;
	private String transactionId;
}
