package com.server.bbo_gak.domain.card.controller;

import com.server.bbo_gak.domain.card.dto.response.CardCreateResponse;
import com.server.bbo_gak.domain.card.dto.response.CardListGetResponse;
import com.server.bbo_gak.domain.card.dto.response.CardTypeCountGetResponse;
import com.server.bbo_gak.domain.card.service.CardInRecruitService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.annotation.AuthUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CardRecruitController {

    private final CardInRecruitService cardInRecruitService;

    @GetMapping("/recruits/{recruit-id}/cards/type-count")
    public ResponseEntity<CardTypeCountGetResponse> getCardTypeCounts(@AuthUser User user) {
        return ResponseEntity.ok(cardInRecruitService.getCardTypeCountsInRecruit(user));
    }

    @GetMapping("/recruits/{recruit-id}/cards/{card-id}")
    public ResponseEntity<List<CardListGetResponse>> getCardDetail(
        @AuthUser User user,
        @PathVariable("recruit-id") Long recruitId,
        @RequestParam("type") String type) {

        return ResponseEntity.ok(cardInRecruitService.getCardListInRecruit(user, recruitId, type));
    }

    @PostMapping("/recruits/{recruit-id}/cards/{card-id}")
    public ResponseEntity<CardCreateResponse> createCard(
        @AuthUser User user,
        @PathVariable("recruit-id") Long recruitId,
        @PathVariable("card-id") Long cardId) {

        return ResponseEntity.ok(cardInRecruitService.copyCardFromMyInfo(user, cardId, recruitId));
    }

}
