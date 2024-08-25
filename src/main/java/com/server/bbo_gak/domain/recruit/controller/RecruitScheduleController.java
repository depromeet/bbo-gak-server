package com.server.bbo_gak.domain.recruit.controller;

import com.server.bbo_gak.domain.recruit.dto.request.RecruitScheduleCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitScheduleGetResponse;
import com.server.bbo_gak.domain.recruit.service.RecruitScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruits/{id}/recruit-schedule")
@RequiredArgsConstructor
public class RecruitScheduleController {
    private final RecruitScheduleService recruitScheduleService;


    /**
     * 공고 일정 생성
     */
    @PostMapping("")
    public ResponseEntity<Void> createRecruitSchedule(
            @PathVariable("id") Long id, //공고 id
            @RequestBody RecruitScheduleCreateRequest request) {
        recruitScheduleService.createRecruitSchedule(id, request);
        return ResponseEntity.ok().body(null);
    }

    /**
     * 공고 일정 전체 조회
     */
    @GetMapping("")
    public ResponseEntity<List<RecruitScheduleGetResponse>> getRecruitScheduleList(
        @PathVariable("id") Long id //공고 id
    ) {
        return ResponseEntity.ok().body(recruitScheduleService.getRecruitScheduleList(id));
    }

}
