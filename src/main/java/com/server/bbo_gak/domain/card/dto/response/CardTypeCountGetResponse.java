package com.server.bbo_gak.domain.card.dto.response;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardType;
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

    public static CardTypeCountGetResponse of(List<Card> cards) {

        Map<String, Long> cardTypeCountMap = cards.stream()
            .collect(Collectors.groupingBy(card -> card.getCardType().getValue(), Collectors.counting()));

        return CardTypeCountGetResponse.builder()
            .경험_정리(cardTypeCountMap.get(CardType.EXPERIENCE.getValue()))
            .면접_질문(cardTypeCountMap.get(CardType.INTERVIEW_QUESTION.getValue()))
            .자기소개서(cardTypeCountMap.get(CardType.PERSONAL_STATEMENT.getValue()))
            .build();
    }

}
