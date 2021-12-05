package com.dagacube.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, disableSubMappingMethodsGeneration = true)
@SuppressWarnings("squid:S1214")
public interface WagerWinRequestMapper {
	WagerWinRequestMapper INSTANCE = Mappers.getMapper(WagerWinRequestMapper.class);

	com.dagacube.domain.model.WagerWinRequest mapToModel(com.dagacube.api.v1.model.WagerWinRequest value, String transactionId);

}
