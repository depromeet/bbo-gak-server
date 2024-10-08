package com.server.bbo_gak.domain.card.dto.response;

import com.server.bbo_gak.domain.card.entity.Tag;
import lombok.Builder;

@Builder
public record TagGetResponse(
    Long id,
    String name,
    String type
) {

    public static TagGetResponse from(Tag tag) {
        return TagGetResponse.builder()
            .id(tag.getId())
            .name(tag.getName())
            .type(tag.getTagType().getValue())
            .build();
    }
}