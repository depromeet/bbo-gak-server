package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByIdAndUserId(Long id, Long userId);

    List<Card> findAllByUserId(Long userId);

    List<Card> findAllByUserIdAndCardType(Long userId, CardType cardType);
}
