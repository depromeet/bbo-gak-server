package com.server.bbo_gak.domain.recruit.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitStatusCategory {

    GENERAL_STATUS(List.of(
        RecruitStatus.PREPARATION_IN_PROGRESS,
        RecruitStatus.APPLICATION_COMPLETED
    )),
    PASSED_STATUS(List.of(
        RecruitStatus.DOCUMENT_PASSED,
        RecruitStatus.INTERVIEW_PASSED,
        RecruitStatus.FINAL_ACCEPTANCE
    )),
    REJECTION_STATUS(List.of(
        RecruitStatus.DOCUMENT_REJECTION,
        RecruitStatus.INTERVIEW_REJECTION,
        RecruitStatus.FINAL_REJECTED
    ));

    private final List<RecruitStatus> statuses;

    public static boolean isRejectionStatusOrFinalAcceptance(RecruitStatus status) {
        return REJECTION_STATUS.getStatuses().contains(status) || RecruitStatus.FINAL_ACCEPTANCE.equals(status);
    }
}
