package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardType;
import com.server.bbo_gak.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByIdAndUser(Long id, User user);

    List<Card> findAllByUser(User user);

    List<Card> findAllByUserAndCardType(User user, CardType cardType);
}
