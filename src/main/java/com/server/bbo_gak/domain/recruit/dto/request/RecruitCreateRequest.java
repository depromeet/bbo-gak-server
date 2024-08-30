package com.server.bbo_gak.domain.recruit.dto.request;

import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.recruit.entity.RecruitStatus;
import com.server.bbo_gak.domain.recruit.entity.Season;
import com.server.bbo_gak.domain.user.entity.User;

public record RecruitCreateRequest(
    String season,
    String title,
    String siteUrl,
    String recruitScheduleStage,
    String deadLine
) {

    public Recruit toEntity(User user, Season season) {
        return Recruit.builder()
            .season(season)
            .title(title)
            .siteUrl(siteUrl)
            .recruitStatus(RecruitStatus.PREPARATION_IN_PROGRESS)
            .user(user)
            .build();
    }

}
