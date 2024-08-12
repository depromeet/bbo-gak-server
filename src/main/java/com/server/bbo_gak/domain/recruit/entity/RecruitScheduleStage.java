package com.server.bbo_gak.domain.recruit.entity;

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
}
