package com.server.bbo_gak.domain.recruit.dto.response;

import com.server.bbo_gak.domain.recruit.entity.Recruit;
import lombok.Builder;

@Builder
public record RecruitGetTitleListResponse(
    Long id,
    String title
) {

    public static RecruitGetTitleListResponse from(Recruit recruit) {
        return RecruitGetTitleListResponse.builder()
            .id(recruit.getId())
            .title(recruit.getTitle())
            .build();
    }
}
