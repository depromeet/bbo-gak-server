package com.server.bbo_gak.domain.card.dto.request;

public record CardImageDeleteRequest(
    Long cardId,
    String staticUrl
) {

}
