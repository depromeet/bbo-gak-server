package com.server.bbo_gak.domain.recruit.controller;

import com.server.bbo_gak.domain.recruit.dto.response.SeasonGetResponse;
import com.server.bbo_gak.domain.recruit.service.SeasonService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.annotation.AuthUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seasons")
@RequiredArgsConstructor
public class SeasonController {

    private final SeasonService seasonService;


    @GetMapping("")
    public ResponseEntity<List<SeasonGetResponse>> getRecruitSeasonList(
        @AuthUser User user
    ) {
        return ResponseEntity.ok(seasonService.getSeasonListByUser(user));
    }
}
