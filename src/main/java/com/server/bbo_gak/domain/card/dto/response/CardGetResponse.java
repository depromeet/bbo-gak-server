package com.server.bbo_gak.domain.card.dto.response;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTag;
import com.server.bbo_gak.global.utils.BaseDateTimeFormatter;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CardGetResponse(
    String title,
    String content,
    String updatedDate,
    String type,
    List<TagGetResponse> tagList
) {

    public static CardGetResponse of(Card card, List<CardTag> cardTagList) {

        List<TagGetResponse> tagGetResponseList = cardTagList.stream()
            .map(cardTag -> TagGetResponse.of(cardTag.getTag()))
            .toList();

        return CardGetResponse.builder()
            .title(card.getTitle())
            .content(card.getContent())
            .updatedDate(card.getUpdatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
            .type(card.getCardType().getValue())
            .tagList(tagGetResponseList)
            .build();
    }
}
