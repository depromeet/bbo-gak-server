package com.server.bbo_gak.domain.recruit.dto.response;

import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
import com.server.bbo_gak.global.utils.BaseDateTimeFormatter;
import lombok.Builder;

@Builder
public record RecruitScheduleGetResponse(
    Long id,
    String recruitScheduleStage,
    String deadLine
) {

    public static RecruitScheduleGetResponse from(RecruitSchedule schedule) {
        if (schedule == null) {
            return null;
        }
        return RecruitScheduleGetResponse.builder()
            .id(schedule.getId())
            .recruitScheduleStage(schedule.getRecruitScheduleStage().getValue())
            .deadLine(schedule.getDeadLine().format(BaseDateTimeFormatter.getLocalDateFormatter()))
            .build();
    }
}
