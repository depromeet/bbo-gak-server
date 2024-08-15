package com.server.bbo_gak.domain.recruit.service;

import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
import org.springframework.stereotype.Service;

@Service
public interface RecruitScheduleService {

    RecruitSchedule createRecruitSchedule(RecruitSchedule recruitSchedule);

    void getRecruitScheduleList();

    void updateRecruitSchedule();

    void deleteRecruitSchedule();
}
