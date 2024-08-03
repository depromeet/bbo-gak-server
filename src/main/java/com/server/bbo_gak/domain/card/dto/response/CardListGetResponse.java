package com.server.bbo_gak.domain.card.dto.response;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTag;
import com.server.bbo_gak.global.utils.BaseDateTimeFormatter;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CardListGetResponse(
    Long id,
    String title,
    String updatedDate,
    List<TagGetResponse> tagList
) {

    public static CardListGetResponse of(Card card, List<CardTag> cardTagList) {

        List<TagGetResponse> tagGetResponseList = cardTagList.stream()
            .map(cardTag -> TagGetResponse.of(cardTag.getTag()))
            .toList();

        return CardListGetResponse.builder()
            .id(card.getId())
            .title(card.getTitle())
            .updatedDate(card.getUpdatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
            .tagList(tagGetResponseList)
            .build();
    }
}
