package com.server.bbo_gak.domain.card.dto.response;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTag;
import com.server.bbo_gak.global.utils.BaseDateTimeFormatter;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CardGetsResponse(
    Long id,
    String title,
    String updateDate,
    List<CardTagResponse> cardTagList
) {

    public static CardGetsResponse of(Card card, List<CardTag> cardTagList) {
        return CardGetsResponse.builder()
            .id(card.getId())
            .title(card.getTitle())
            .updateDate(card.getUpdatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
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
