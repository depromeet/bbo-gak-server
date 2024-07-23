package com.server.bbo_gak.domain.card.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record TagsGetResponse(
    Long id,
    String name,
    String type
) {

}
