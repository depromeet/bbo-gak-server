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
    String updateDate,
    String type,
    List<CardTagResponse> cardTagList
) {

    public static CardGetResponse of(Card card, List<CardTag> cardTagList) {
        return CardGetResponse.builder()
            .title(card.getTitle())
            .content(card.getContent())
            .updateDate(card.getUpdatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
            .type(card.getCardType().getValue())
            .cardTagList(cardTagList.stream().map(CardTagResponse::of).toList())
            .build();
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record CardTagResponse(
        Long id,
        String name,
        String type
    ) {

        public static CardTagResponse of(CardTag cardTag) {
            return CardTagResponse.builder()
                .id(cardTag.getId())
                .name(cardTag.getTag().getName())
                .type(cardTag.getTag().getTagType().getValue())
                .build();
        }
    }
}
