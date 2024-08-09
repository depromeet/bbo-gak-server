package com.server.bbo_gak.domain.recruit.dto.response;

import com.server.bbo_gak.domain.recruit.entity.Season;

public record SeasonGetResponse(
    String name
) {

    public static SeasonGetResponse from(Season season) {
        return new SeasonGetResponse(season.getName());
    }
}
