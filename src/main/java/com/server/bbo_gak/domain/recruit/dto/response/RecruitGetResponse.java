package com.server.bbo_gak.domain.recruit.dto.response;

import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.global.utils.BaseDateTimeFormatter;
import lombok.Builder;

@Builder
public record RecruitGetResponse(
    Long id,
    String title,
    String season,
    String siteUrl,
    String recruitStatus,
    String createdDate
) {

    public static RecruitGetResponse from(Recruit recruit) {

        return RecruitGetResponse.builder()
            .id(recruit.getId())
            .title(recruit.getTitle())
            .season(recruit.getSeason().getName())
            .siteUrl(recruit.getSiteUrl())
            .recruitStatus(recruit.getRecruitStatus().getValue())
            .createdDate(recruit.getCreatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
            .build();
    }
}
