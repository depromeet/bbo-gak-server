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

    void updateRecruitSchedule();

    void deleteRecruitSchedule();
}
