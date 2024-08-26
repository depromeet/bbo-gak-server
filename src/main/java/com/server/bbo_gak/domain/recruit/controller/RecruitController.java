package com.server.bbo_gak.domain.recruit.controller;

import com.server.bbo_gak.domain.recruit.dto.request.RecruitCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateSeasonRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateSiteUrlRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateStatusRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateTitleRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetInnerResponse;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetResponse;
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
    public ResponseEntity<List<RecruitGetResponse>> getTotalRecruitList(
        @AuthUser User user
    ) {
        return ResponseEntity.ok(recruitService.getTotalRecruitList(user));
    }

    @GetMapping("{id}")
    public ResponseEntity<RecruitGetInnerResponse> getRecruitInnerInfo(
        @AuthUser User user,
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(recruitService.getRecruit(user, id));
    }


    @GetMapping("/bySeason")
    public ResponseEntity<List<RecruitGetResponse>> getRecruitListBySeason(
        @AuthUser User user,
        @RequestParam("season") String season
    ) {
        return ResponseEntity.ok(recruitService.getRecruitListBySeason(user, season));
    }

    @GetMapping("/progressing")
    public ResponseEntity<List<RecruitGetResponse>> getProgressingRecruitList(
        @AuthUser User user
    ) {
        return ResponseEntity.ok(recruitService.getProgressingRecruitList(user));
    }

    @PostMapping("")
    public ResponseEntity<RecruitGetResponse> createRecruit(
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
    public ResponseEntity<RecruitGetResponse> updateRecruitTitle(
        @AuthUser User user,
        @PathVariable("id") Long id,
        @RequestBody RecruitUpdateTitleRequest request) {
        RecruitGetResponse response = recruitService.updateRecruitTitle(user, id, request.title());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/season")
    public ResponseEntity<RecruitGetResponse> updateRecruitSeason(
        @AuthUser User user,
        @PathVariable("id") Long id,
        @RequestBody RecruitUpdateSeasonRequest request) {
        RecruitGetResponse response = recruitService.updateRecruitSeason(user, id, request.season());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RecruitGetResponse> updateRecruitStatus(
        @AuthUser User user,
        @PathVariable("id") Long id,
        @RequestBody RecruitUpdateStatusRequest request) {
        RecruitGetResponse response = recruitService.updateRecruitStatus(user, id, request.recruitStatus());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/siteUrl")
    public ResponseEntity<RecruitGetResponse> updateRecruitSiteUrl(
        @AuthUser User user,
        @PathVariable("id") Long id,
        @RequestBody RecruitUpdateSiteUrlRequest request) {
        RecruitGetResponse response = recruitService.updateRecruitSiteUrl(user, id, request.siteUrl());
        return ResponseEntity.ok(response);
    }
}
