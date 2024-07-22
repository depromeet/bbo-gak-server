package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardImageRepository extends JpaRepository<CardImage, Long> {

    void deleteByCardAndImageUrl(Card card, String imageUrl);
}
