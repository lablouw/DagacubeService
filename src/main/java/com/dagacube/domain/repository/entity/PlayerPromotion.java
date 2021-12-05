package com.dagacube.domain.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class PlayerPromotion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@JoinColumn(name = "player_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Player player;

	@JoinColumn(name = "promotion_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Promotion promotion;

	private String value;

}
