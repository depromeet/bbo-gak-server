package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTypeRepository extends JpaRepository<CardType, Long> {

}
