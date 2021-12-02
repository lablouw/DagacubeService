package com.dagacube.api.v1.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class PlayerTransactionsRequest {
	@NotBlank
	private String username;
	@NotBlank
	private String password;//If you add your system username too then everyone could have unique passwords! We should talk, Pam.
	private Integer count;//defaults to 10 if null
}
