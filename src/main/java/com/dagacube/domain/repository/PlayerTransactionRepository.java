package com.dagacube.domain.repository;

import com.dagacube.domain.repository.entity.PlayerTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerTransactionRepository extends JpaRepository<PlayerTransaction, Long> {

	Optional<PlayerTransaction> findByTransactionId(String transactionId);

	List<PlayerTransaction> findAllByPlayerIdOrderByCreatedDesc(long playerId, Pageable pageable);
}
