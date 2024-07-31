package com.server.bbo_gak.domain.card.dto.response;

import com.server.bbo_gak.domain.card.entity.CardMemo;
import com.server.bbo_gak.global.utils.BaseDateTimeFormatter;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CardMemoGetResponse(
    Long id,
    String content,
    String updatedDate
) {

    public static CardMemoGetResponse of(CardMemo cardMemo) {
        return CardMemoGetResponse.builder()
            .id(cardMemo.getId())
            .content(cardMemo.getContent())
            .updatedDate(cardMemo.getUpdatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
            .build();
    }
}