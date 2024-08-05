package com.server.bbo_gak.domain.card.dto.response;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardType;
import com.server.bbo_gak.domain.card.entity.CardTypeValue;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CardTypeCountGetResponse(
    Long 경험_정리,
    Long 자기소개서,
    Long 면접_질문
) {

    public static CardTypeCountGetResponse from(List<Card> cards) {

        Map<CardTypeValue, Long> cardTypeValueCountMap = cards.stream()
            .flatMap(card -> card.getCardTypeList().stream())
            .collect(Collectors.groupingBy(CardType::getCardTypeValue, Collectors.counting()));

        return CardTypeCountGetResponse.builder()
            .경험_정리(cardTypeValueCountMap.getOrDefault(CardTypeValue.EXPERIENCE, 0L))
            .자기소개서(cardTypeValueCountMap.getOrDefault(CardTypeValue.PERSONAL_STATEMENT, 0L))
            .면접_질문(cardTypeValueCountMap.getOrDefault(CardTypeValue.INTERVIEW_QUESTION, 0L))
            .build();
    }

}
