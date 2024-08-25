package com.server.bbo_gak.domain.recruit.dto.request;

import com.server.bbo_gak.domain.recruit.entity.RecruitScheduleStage;
import lombok.Builder;

@Builder
public record RecruitScheduleCreateRequest(
    RecruitScheduleStage recruitScheduleStage,
    String deadline
) {

    public static RecruitScheduleCreateRequest of(RecruitScheduleStage recruitScheduleStage, String deadline) {
        return RecruitScheduleCreateRequest.builder()
            .recruitScheduleStage(recruitScheduleStage)
            .deadline(deadline)
            .build();
    }
}
