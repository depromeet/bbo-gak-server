package com.server.bbo_gak.domain.card.dao;

import com.querydsl.core.BooleanBuilder;
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

    public List<Card> findAllByUserIdAndCardTypeValueList(User user, CardTypeValue[] cardTypeValueList,
        boolean isRecruit) {

        QCard qCard = QCard.card;
        QCardType qCardType = QCardType.cardType;

        createRecruitBooleanBuilder(qCard, isRecruit);

        return query.selectFrom(qCard)
            .leftJoin(qCard.cardTypeList, qCardType).fetchJoin()
            .where(qCard.user.id.eq(user.getId())
                .and(qCardType.cardTypeValue.in(cardTypeValueList))
                .and(createRecruitBooleanBuilder(qCard, isRecruit)))
            .distinct()
            .fetch();
    }

    public List<Card> findAllByUserIdAndCardTypeValue(User user, CardTypeValue cardTypeValue, Long recruitId) {

        QCard qCard = QCard.card;
        QCardType qCardType = QCardType.cardType;

        createRecruitBooleanBuilder(qCard, recruitId);

        return query.selectFrom(qCard)
            .leftJoin(qCard.cardTypeList, qCardType).fetchJoin()
            .where(qCard.user.id.eq(user.getId())
                .and(qCardType.cardTypeValue.eq(cardTypeValue))
                .and(createRecruitBooleanBuilder(qCard, recruitId)))
            .distinct()
            .fetch();
    }

    private BooleanBuilder createRecruitBooleanBuilder(QCard qCard, boolean isRecruit) {

        BooleanBuilder builder = new BooleanBuilder();

        if (isRecruit) {
            return builder.and(qCard.recruit.isNotNull());
        }

        return builder.and(qCard.recruit.isNull());
    }

    private BooleanBuilder createRecruitBooleanBuilder(QCard qCard, Long recruitId) {

        BooleanBuilder builder = new BooleanBuilder();

        if (recruitId == null) {
            return builder.and(qCard.recruit.isNull()).and(qCard.copyFlag.isFalse());
        }

        return builder.and(qCard.recruit.id.eq(recruitId)).and(qCard.copyFlag.isTrue());

    }
}