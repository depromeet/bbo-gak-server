package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardMemo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardMemoRepository extends JpaRepository<CardMemo, Long> {

    Optional<CardMemo> findByIdAndCard(Long id, Card card);
}
