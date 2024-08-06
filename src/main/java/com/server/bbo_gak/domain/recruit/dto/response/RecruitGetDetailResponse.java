package com.server.bbo_gak.domain.recruit.dto.response;

import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.recruit.entity.RecruitStatus;
import com.server.bbo_gak.global.utils.BaseDateTimeFormatter;
import java.util.List;
import lombok.Builder;

@Builder
public record RecruitGetDetailResponse(
    Long id,
    String title,
    String season,
    String siteUrl,
    RecruitStatus recruitStatus,
    String createdDate,
    List<RecruitScheduleGetResponse> recruitScheduleGetResponses
) {

    public static RecruitGetDetailResponse from(Recruit recruit) {
        List<RecruitScheduleGetResponse> scheduleGetResponses = recruit.getScheduleList().stream()
            .map(RecruitScheduleGetResponse::from)
            .toList();

        return RecruitGetDetailResponse.builder()
            .id(recruit.getId())
            .title(recruit.getTitle())
            .recruitScheduleGetResponses(scheduleGetResponses)
            .season(recruit.getSeason().getName())
            .siteUrl(recruit.getSiteUrl())
            .recruitStatus(recruit.getRecruitStatus())
            .createdDate(recruit.getCreatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
            .build();
    }
}
