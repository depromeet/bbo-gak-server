package com.server.bbo_gak.domain.card.dto.response;

import com.server.bbo_gak.domain.card.entity.Tag;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record TagGetResponse(
    Long id,
    String name,
    String type
) {

    public static TagGetResponse of(Tag tag) {
        return TagGetResponse.builder()
            .id(tag.getId())
            .name(tag.getName())
            .type(tag.getTagType().getValue())
            .build();
    }
}