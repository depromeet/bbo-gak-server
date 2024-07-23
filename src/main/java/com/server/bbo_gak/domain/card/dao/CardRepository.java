package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

}
