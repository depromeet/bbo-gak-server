package com.server.bbo_gak.domain.card.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTypeValue;
import com.server.bbo_gak.domain.card.entity.QCard;
import com.server.bbo_gak.domain.card.entity.QCardType;
import com.server.bbo_gak.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class CardDao {

    private final JPAQueryFactory query;

    public List<Card> findAllByUserIdAndCardTypeValueList(User user, CardTypeValue[] cardTypeValueList) {

        QCard qCard = QCard.card;
        QCardType qCardType = QCardType.cardType;

        return query.selectFrom(qCard)
            .leftJoin(qCard.cardTypeList, qCardType).fetchJoin()
            .where(qCard.user.id.eq(user.getId()).and(qCardType.cardTypeValue.in(cardTypeValueList))).distinct()
            .fetch();
    }

    public List<Card> findAllByUserIdAndCardTypeValue(User user, CardTypeValue cardTypeValue) {

        QCard qCard = QCard.card;
        QCardType qCardType = QCardType.cardType;

        return query.selectFrom(qCard)
            .leftJoin(qCard.cardTypeList, qCardType).fetchJoin()
            .where(qCard.user.id.eq(user.getId()).and(qCardType.cardTypeValue.eq(cardTypeValue))).distinct()
            .fetch();
    }


}
