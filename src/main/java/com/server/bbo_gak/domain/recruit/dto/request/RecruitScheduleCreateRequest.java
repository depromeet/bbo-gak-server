package com.server.bbo_gak.domain.recruit.dto.request;

import lombok.Builder;

@Builder
public record RecruitScheduleCreateRequest(
    String recruitScheduleStage,
    String deadLine
) {

    public static RecruitScheduleCreateRequest of(String recruitScheduleStage, String deadLine) {
        return RecruitScheduleCreateRequest.builder()
            .recruitScheduleStage(recruitScheduleStage)
            .deadLine(deadLine)
            .build();
    }
}
