package com.dagacube.domain.model;

import com.dagacube.domain.model.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class PlayerTransaction {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	@Column(name = "created")
	private Date created;

	@Column(name = "player")
	private long playerId;

	@Column(name = "transactionType")
	private TransactionType transactionType;

	@Column(name = "balance")
	private BigDecimal balance;

}
