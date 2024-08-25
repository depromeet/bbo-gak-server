package com.server.bbo_gak.domain.recruit.service;

import com.server.bbo_gak.domain.recruit.dao.RecruitRepository;
import com.server.bbo_gak.domain.recruit.dao.RecruitScheduleRepository;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitScheduleCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitScheduleGetResponse;
import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitScheduleServiceImpl implements RecruitScheduleService {

    private final RecruitScheduleRepository recruitScheduleRepository;
    private final RecruitRepository recruitRepository;

    @Override
    public void deleteRecruitSchedule() {
        
    }

    @Override
    public void updateRecruitSchedule() {

    }

    @Override
    public List<RecruitScheduleGetResponse> getRecruitScheduleList(Long recruitId) {
        return recruitScheduleRepository.findAllByRecruitId(recruitId).stream()
                .map(RecruitScheduleGetResponse::from)
                .toList();
    }

    @Override
    public RecruitSchedule createRecruitSchedule(Long id, RecruitScheduleCreateRequest request) {
        Recruit recruit = recruitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RECRUIT_NOT_FOUND));
        return recruitScheduleRepository.save(RecruitSchedule.of(recruit, request.recruitScheduleStage(), request.deadline()));
    }
}
