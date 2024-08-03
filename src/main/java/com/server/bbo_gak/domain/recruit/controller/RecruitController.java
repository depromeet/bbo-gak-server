package com.server.bbo_gak.domain.recruit.controller;

import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetResponse;
import com.server.bbo_gak.domain.recruit.service.RecruitService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.annotation.AuthUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("/bySeason")
    public ResponseEntity<List<RecruitGetResponse>> getRecruitListBySeason(
        @AuthUser User user,
        @RequestParam("season") String season
    ) {
        return ResponseEntity.ok(recruitService.getRecruitListBySeason(user, season));
    }


}
