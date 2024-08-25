package com.server.bbo_gak.domain.recruit.dto.request;

import lombok.Builder;

@Builder
public record RecruitScheduleCreateRequest(
    String recruitScheduleStage,
    String deadline
) {

    public static RecruitScheduleCreateRequest of(String recruitScheduleStage, String deadline) {
        return RecruitScheduleCreateRequest.builder()
            .recruitScheduleStage(recruitScheduleStage)
            .deadline(deadline)
            .build();
    }
}
