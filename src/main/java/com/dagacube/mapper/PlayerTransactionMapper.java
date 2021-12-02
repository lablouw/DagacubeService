package com.dagacube.mapper;

import com.dagacube.domain.repository.entity.PlayerTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, disableSubMappingMethodsGeneration = true)
@SuppressWarnings("squid:S1214")
public interface PlayerTransactionMapper {
	PlayerTransactionMapper INSTANCE = Mappers.getMapper(PlayerTransactionMapper.class);

	List<com.dagacube.api.v1.model.PlayerTransaction> mapToV1(List<PlayerTransaction> value);

	com.dagacube.api.v1.model.PlayerTransaction mapToV1(PlayerTransaction value);

}
