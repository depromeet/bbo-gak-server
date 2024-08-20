package com.server.bbo_gak.domain.recruit.service;

import com.server.bbo_gak.domain.recruit.dao.RecruitScheduleRepository;
import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitScheduleServiceImpl implements RecruitScheduleService {

    private final RecruitScheduleRepository recruitScheduleRepository;

    @Override
    public void deleteRecruitSchedule() {
        
    }

    @Override
    public void updateRecruitSchedule() {

    }

    @Override
    public void getRecruitScheduleList() {

    }

    @Override
    public RecruitSchedule createRecruitSchedule(RecruitSchedule recruitSchedule) {
        return recruitScheduleRepository.save(recruitSchedule);
    }
}
