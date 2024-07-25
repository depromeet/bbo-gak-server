package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTag;
import com.server.bbo_gak.domain.card.entity.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTagRepository extends JpaRepository<CardTag, Long> {

    List<CardTag> findAllByCard(Card card);

    Optional<CardTag> findByCardAndTag(Card card, Tag tag);

}
