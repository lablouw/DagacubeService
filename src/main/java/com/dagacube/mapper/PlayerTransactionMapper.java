package com.dagacube.mapper;

import com.dagacube.api.v1.model.PlayerTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, disableSubMappingMethodsGeneration = true)
@SuppressWarnings("squid:S1214")
public interface PlayerTransactionMapper {
	PlayerTransactionMapper INSTANCE = Mappers.getMapper(PlayerTransactionMapper.class);

	List<PlayerTransaction> mapToV1(List<com.dagacube.domain.model.PlayerTransaction> value);

}
