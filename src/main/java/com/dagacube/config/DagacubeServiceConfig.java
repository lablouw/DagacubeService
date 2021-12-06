package com.dagacube.config;

import com.dagacube.domain.service.PromotionService;
import com.dagacube.domain.service.PromotionServiceImpl;
import com.dagacube.domain.service.PlayerService;
import com.dagacube.aspect.RestBoundaryLoggerAspect;
import com.dagacube.domain.service.PlayerServiceImpl;
import com.dagacube.security.SecurityService;
import com.dagacube.security.SecurityServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DagacubeServiceConfig {

	@Bean
	public RestBoundaryLoggerAspect restBoundaryLoggerAspect() {
		return new RestBoundaryLoggerAspect();
	}

	@Bean
	public PlayerService playerService() {
		return new PlayerServiceImpl();
	}

	@Bean
	public SecurityService securityService() {
		return new SecurityServiceImpl();
	}

	@Bean
	public PromotionService promotionService() {
		return new PromotionServiceImpl();
	}

}
