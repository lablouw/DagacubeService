package com.dagacube.api.v1.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel
public class Player {
	@NotBlank
	private String username;
	@NotNull
	private BigDecimal balance;
}
