package com.server.bbo_gak.domain.card.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CardCreateResponse(
    Long cardId
) {

    public static CardCreateResponse of(Long cardId) {
        return CardCreateResponse.builder()
            .cardId(cardId)
            .build();
    }
}
