package com.server.bbo_gak.domain.card.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CardCreateResponse(
    Long cardId,
    String type
) {

    public static CardCreateResponse of(Long cardId, String type) {
        return CardCreateResponse.builder()
            .cardId(cardId)
            .type(type)
            .build();
    }
}
