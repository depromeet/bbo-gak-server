package com.server.bbo_gak.domain.recruit.entity;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitStatus {

    PREPARATION_IN_PROGRESS("지원 준비"),
    APPLICATION_COMPLETED("지원 완료"),
    DOCUMENT_PASSED("서류 통과"),
    DOCUMENT_REJECTION("서류 탈락"),
    INTERVIEW_PASSED("면접 통과"),
    INTERVIEW_REJECTION("서류 탈락"),
    FINAL_ACCEPTANCE("최종 합격"),
    FINAL_REJECTED("최종 탈락");

    private final String value;

    public static RecruitStatus findByValue(String value) {
        return Arrays.stream(RecruitStatus.values())
            .filter(recruitStatus -> recruitStatus.getValue().equals(value))
            .findFirst()
            .orElseThrow((() -> new NotFoundException(ErrorCode.RECRUIT_STATUS_NOT_FOUND)));
    }
}
