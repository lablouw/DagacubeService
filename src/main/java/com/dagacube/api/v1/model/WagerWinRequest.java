package com.dagacube.api.v1.model;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@Builder
@ApiModel
public class WagerWinRequest {
	@NotNull
	private Long playerId;
	@NotNull
	@PositiveOrZero
	private BigDecimal amount;
	private String promoCode;
}
