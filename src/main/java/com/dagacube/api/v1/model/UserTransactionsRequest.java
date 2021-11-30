package com.dagacube.api.v1.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class UserTransactionsRequest {
	@NotBlank
	private String userName;
	@NotBlank
	private String password;
	private Integer count;
}
