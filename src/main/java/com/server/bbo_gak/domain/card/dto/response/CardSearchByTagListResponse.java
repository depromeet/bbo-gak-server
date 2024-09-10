package com.server.bbo_gak.domain.card.dto.response;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTypeValue;
import com.server.bbo_gak.domain.card.entity.CardTypeValueGroup;
import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.global.utils.BaseDateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CardSearchByTagListResponse(
    Long id,
    String title,
    String updatedDate,
    List<TagGetResponse> tagList,
    String cardTypeValueGroup,
    String cardTypeValue,
    String recruitTitle,
    String content
) {

    public static CardSearchByTagListResponse from(Card card) {

        CardTypeValue cardTypeValue = card.getCardTypeList().getFirst().getCardTypeValue();

        return CardSearchByTagListResponse.builder()
            .id(card.getId())
            .title(card.getTitle())
            .updatedDate(card.getUpdatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
            .tagList(
                card.getCardTagList().stream()
                    .map(cardTag -> TagGetResponse.from(cardTag.getTag()))
                    .toList()
            )
            .cardTypeValueGroup(CardTypeValueGroup.findByCardTypeValue(cardTypeValue).getValue())
            .recruitTitle(Optional.ofNullable(card.getRecruit()).map(Recruit::getTitle).orElse(null))
            .cardTypeValue(cardTypeValue.getValue())
            .content(card.getContent())
            .build();
    }
}
