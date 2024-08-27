package com.server.bbo_gak.domain.recruit.entity;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitScheduleStage {

    CLOSING_DOCUMENT("서류 마감"),
    FIRST_INTERVIEW("1차 면접"),
    SECOND_INTERVIEW("2차 면접"),
    THIRD_INTERVIEW("3차 면접"),
    FINAL_INTERVIEW("최종 면접");

    private final String Value;

    public static RecruitScheduleStage findByValue(String value) {
        return Arrays.stream(RecruitScheduleStage.values())
            .filter(scheduleStage -> scheduleStage.getValue().equals(value))
            .findFirst()
            .orElseThrow((() -> new NotFoundException(ErrorCode.RECRUIT_SCHEDULE_STAGE_NOT_FOUND)));
    }
}
