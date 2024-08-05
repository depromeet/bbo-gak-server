package com.server.bbo_gak.domain.card.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CardCreateResponse(
    Long cardId
) {

    public static CardCreateResponse of(Long cardId, List<String> cardTypeValueList, List<Long> tagIdList) {
        return CardCreateResponse.builder()
            .cardId(cardId)
            .build();
    }
}
