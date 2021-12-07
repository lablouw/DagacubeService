package com.dagacube.config;

import com.dagacube.domain.service.PromotionService;
import com.dagacube.domain.service.PromotionServiceImpl;
import com.dagacube.domain.service.PlayerService;
import com.dagacube.aspect.RestBoundaryLoggerAspect;
import com.dagacube.domain.service.PlayerServiceImpl;
import com.dagacube.domain.service.SecurityService;
import com.dagacube.domain.service.SecurityServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
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
