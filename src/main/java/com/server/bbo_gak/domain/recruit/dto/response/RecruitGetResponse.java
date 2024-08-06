package com.server.bbo_gak.domain.recruit.dto.response;

import com.server.bbo_gak.domain.recruit.entity.RecruitStatus;

public record RecruitGetResponse(
    Long id,
    String title,
    String season,
    RecruitStatus recruitStatus
) {

}
