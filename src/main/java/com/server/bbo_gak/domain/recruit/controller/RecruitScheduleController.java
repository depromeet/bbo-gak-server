package com.server.bbo_gak.domain.recruit.controller;

import com.server.bbo_gak.domain.recruit.dto.request.RecruitScheduleCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitScheduleUpdateDeadLineRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitScheduleUpdateStageRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitScheduleGetResponse;
import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
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
    public ResponseEntity<RecruitScheduleGetResponse> createRecruitSchedule(
            @PathVariable("id") Long id, //공고 id
            @RequestBody RecruitScheduleCreateRequest request) {
        RecruitSchedule recruitSchedule = recruitScheduleService.createRecruitSchedule(id, request);
        return ResponseEntity.ok().body(RecruitScheduleGetResponse.from(recruitSchedule));
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

    /**
     * 공고 일정 (공고단계) 업데이트
     */
    @PutMapping("/{recruit-schedule-id}/stage")
    public ResponseEntity<Void> updateRecruitScheduleStage(
        @PathVariable("id") Long id, //공고 id
        @PathVariable("recruit-schedule-id") Long recruitScheduleId,
        @RequestBody RecruitScheduleUpdateStageRequest request
    ) {
        recruitScheduleService.updateRecruitScheduleStage(id, recruitScheduleId, request.recruitScheduleStage());
        return ResponseEntity.ok().body(null);
    }

    /**
     * 공고 일정 (공고 마감일) 업데이트
     */
    @PutMapping("/{recruit-schedule-id}/deadLine")
    public ResponseEntity<Void> updateDeadLine(
        @PathVariable("id") Long id, //공고 id
        @PathVariable("recruit-schedule-id") Long recruitScheduleId,
        @RequestBody RecruitScheduleUpdateDeadLineRequest request
    ) {
        recruitScheduleService.updateDeadLine(id, recruitScheduleId, request.deadLine());
        return ResponseEntity.ok().body(null);
    }

    /**
     * 공고 일정 삭제
     */
    @DeleteMapping("/{recruit-schedule-id}")
    public ResponseEntity<Void> deleteRecruitSchedule(
        @PathVariable("id") Long id, //공고 id
        @PathVariable("recruit-schedule-id") Long recruitScheduleId
    ) {
        recruitScheduleService.deleteRecruitSchedule(id, recruitScheduleId);
        return ResponseEntity.ok().body(null);
    }
}
