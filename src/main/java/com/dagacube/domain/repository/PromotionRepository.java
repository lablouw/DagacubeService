package com.dagacube.domain.repository;

import com.dagacube.domain.repository.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

	Promotion getByCode(String code);
}
