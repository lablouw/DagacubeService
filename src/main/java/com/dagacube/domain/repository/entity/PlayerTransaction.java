package com.dagacube.domain.repository.entity;

import com.dagacube.domain.types.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class PlayerTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false)
	private Date created;

	@JoinColumn(name = "player_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Player player;

	@Column(nullable = false)
	private TransactionType transactionType;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private BigDecimal postBalance;

	@Column(nullable = false, unique = true)
	private String transactionId;

}
