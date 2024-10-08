package com.server.bbo_gak.domain.card.dto.response;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTag;
import com.server.bbo_gak.domain.card.entity.CardTypeValueGroup;
import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.global.utils.BaseDateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Builder
public record CardGetResponse(
    String title,
    String content,
    String createdDate,
    String updatedDate,
    String recruitTitle,
    String cardTypeValueGroup,
    List<String> cardTypeValueList,
    List<TagGetResponse> tagList
) {

    public static CardGetResponse of(Card card, List<CardTag> cardTagList) {

        List<TagGetResponse> tagGetResponseList = cardTagList.stream()
            .map(cardTag -> TagGetResponse.from(cardTag.getTag()))
            .toList();

        List<String> cardTypeValueList = card.getCardTypeList().stream()
            .map(cardType -> cardType.getCardTypeValue().getValue())
            .toList();

        String recruitTitle = Optional.ofNullable(card.getRecruit())
            .map(Recruit::getTitle)
            .orElse(null);

        return CardGetResponse.builder()
            .title(card.getTitle())
            .content(card.getContent())
            .recruitTitle(recruitTitle)
            .cardTypeValueGroup(
                CardTypeValueGroup.findByCardTypeValue(card.getCardTypeList().getFirst().getCardTypeValue()).getValue())
            .createdDate(card.getCreatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
            .updatedDate(card.getUpdatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
            .cardTypeValueList(cardTypeValueList)
            .tagList(tagGetResponseList)
            .build();
    }
}
