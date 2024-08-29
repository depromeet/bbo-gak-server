package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardCopyInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardCopyInfoRepository extends JpaRepository<CardCopyInfo, Long> {

    boolean existsByCard(Card card);

    Optional<CardCopyInfo> findByCard(Card card);
}
