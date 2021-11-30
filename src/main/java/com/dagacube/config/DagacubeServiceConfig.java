package com.dagacube.config;

import com.dagacube.domain.service.UserWalletService;
import com.dagacube.aspect.RestBoundaryLoggerAspect;
import com.dagacube.domain.service.UserWalletServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DagacubeServiceConfig {

	@Bean
	public RestBoundaryLoggerAspect restBoundaryLoggerAspect() {
		return new RestBoundaryLoggerAspect();
	}

	@Bean
	public UserWalletService rankingsManager() {
		return new UserWalletServiceImpl();
	}

}
