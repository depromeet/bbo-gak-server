package com.server.bbo_gak.domain.recruit.dto.response;

import com.server.bbo_gak.domain.recruit.entity.Recruit;
import lombok.Builder;

@Builder
public record RecruitGetInnerResponse(
    Long id,
    String title,
    String season,
    String siteUrl,
    String recruitStatus
) {

    public static RecruitGetInnerResponse from(Recruit recruit) {

        return RecruitGetInnerResponse.builder()
            .id(recruit.getId())
            .title(recruit.getTitle())
            .season(recruit.getSeason().getName())
            .siteUrl(recruit.getSiteUrl())
            .recruitStatus(recruit.getRecruitStatus().getValue())
            .build();
    }
}
