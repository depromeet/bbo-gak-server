package com.server.bbo_gak.domain.recruit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitStatus {

    PREPARATION_IN_PROGRESS("지원 준비 중"),
    APPLICATION_COMPLETED("지원 완료"),
    DOCUMENT_PASSED("서류 통과"),
    INTERVIEW_PASSED("면접 통과"),
    FINAL_ACCEPTANCE("최종 합격"),
    REJECTED("불합격");


    private final String value;
}
