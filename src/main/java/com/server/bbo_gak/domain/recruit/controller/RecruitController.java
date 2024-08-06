package com.server.bbo_gak.domain.recruit.controller;

import com.server.bbo_gak.domain.recruit.dto.request.RecruitCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateSeasonRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateSiteUrlRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateStatusRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateTitleRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetDetailResponse;
import com.server.bbo_gak.domain.recruit.service.RecruitService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.annotation.AuthUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recruits")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;

    @GetMapping("")
    public ResponseEntity<List<RecruitGetDetailResponse>> getTotalRecruitList(
        @AuthUser User user
    ) {
        return ResponseEntity.ok(recruitService.getTotalRecruitList(user));
    }


    @GetMapping("/bySeason")
    public ResponseEntity<List<RecruitGetDetailResponse>> getRecruitListBySeason(
        @AuthUser User user,
        @RequestParam("season") String season
    ) {
        return ResponseEntity.ok(recruitService.getRecruitListBySeason(user, season));
    }

    @GetMapping("/progressing")
    public ResponseEntity<List<RecruitGetDetailResponse>> getProgressingRecruitList(
        @AuthUser User user
    ) {
        return ResponseEntity.ok(recruitService.getProgressingRecruitList(user));
    }

    @PostMapping("")
    public ResponseEntity<Long> createRecruit(
        @AuthUser User user,
        @RequestBody RecruitCreateRequest request
    ) {
        return ResponseEntity.ok(recruitService.createRecruit(user, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruit(
        @AuthUser User user,
        @PathVariable("id") Long id
    ) {
        recruitService.deleteRecruit(user, id);
        return ResponseEntity.ok().body(null);
    }

    @PatchMapping("/{id}/title")
    public ResponseEntity<RecruitGetDetailResponse> updateRecruitTitle(
        @AuthUser User user,
        @PathVariable("id") Long id,
        @RequestBody RecruitUpdateTitleRequest request) {
        RecruitGetDetailResponse response = recruitService.updateRecruitTitle(user, id, request.title());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/season")
    public ResponseEntity<RecruitGetDetailResponse> updateRecruitSeason(
        @AuthUser User user,
        @PathVariable("id") Long id,
        @RequestBody RecruitUpdateSeasonRequest request) {
        RecruitGetDetailResponse response = recruitService.updateRecruitSeason(user, id, request.season());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RecruitGetDetailResponse> updateRecruitStatus(
        @AuthUser User user,
        @PathVariable("id") Long id,
        @RequestBody RecruitUpdateStatusRequest request) {
        RecruitGetDetailResponse response = recruitService.updateRecruitStatus(user, id, request.recruitStatus());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/siteUrl")
    public ResponseEntity<RecruitGetDetailResponse> updateRecruitSiteUrl(
        @AuthUser User user,
        @PathVariable("id") Long id,
        @RequestBody RecruitUpdateSiteUrlRequest request) {
        RecruitGetDetailResponse response = recruitService.updateRecruitSiteUrl(user, id, request.siteUrl());
        return ResponseEntity.ok(response);
    }
}
