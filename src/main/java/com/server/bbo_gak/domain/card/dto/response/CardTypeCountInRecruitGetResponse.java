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
public record CardTypeCountInRecruitGetResponse(
    Long 서류_준비,
    Long 과제_준비,
    Long 인터뷰_준비
) {

    public static CardTypeCountInRecruitGetResponse from(List<Card> cards) {

        Map<CardTypeValue, Long> cardTypeValueCountMap = cards.stream()
            .flatMap(card -> card.getCardTypeList().stream())
            .collect(Collectors.groupingBy(CardType::getCardTypeValue, Collectors.counting()));

        return CardTypeCountInRecruitGetResponse.builder()
            .서류_준비(cardTypeValueCountMap.getOrDefault(CardTypeValue.DOCUMENT_PREPARING, 0L))
            .과제_준비(cardTypeValueCountMap.getOrDefault(CardTypeValue.ASSIGNMENT_PREPARING, 0L))
            .인터뷰_준비(cardTypeValueCountMap.getOrDefault(CardTypeValue.INTERVIEW_PREPARING, 0L))
            .build();
    }

}
