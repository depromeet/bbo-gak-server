package com.server.bbo_gak.domain.recruit.service;

import com.server.bbo_gak.domain.recruit.dto.request.RecruitScheduleCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitScheduleGetResponse;
import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecruitScheduleService {

    RecruitSchedule createRecruitSchedule(Long id, RecruitScheduleCreateRequest request);

    List<RecruitScheduleGetResponse> getRecruitScheduleList(Long recruitId);

    void updateRecruitScheduleStage(Long recruitId, Long recruitScheduleId, String stage);

    void updateDeadLine(Long recruitId, Long recruitScheduleId, String deadLine);

    void deleteRecruitSchedule(Long recruitId, Long recruitScheduleId);
}
