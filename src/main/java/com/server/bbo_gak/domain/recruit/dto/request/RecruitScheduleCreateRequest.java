package com.server.bbo_gak.domain.recruit.dto.request;

import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
import com.server.bbo_gak.domain.recruit.entity.RecruitScheduleStage;
import java.time.LocalDate;

public record RecruitScheduleCreateRequest(
    RecruitScheduleStage recruitScheduleStage,
    String deadline
) {

    public static RecruitSchedule of(RecruitScheduleStage recruitScheduleStage, String deadline) {
        return RecruitSchedule.builder()
            .recruitScheduleStage(recruitScheduleStage)
            .deadLine(LocalDate.parse(deadline))
            .build();
    }
}
