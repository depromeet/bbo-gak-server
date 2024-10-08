package com.server.bbo_gak.domain.card.controller;

import com.server.bbo_gak.domain.card.dto.request.CardContentUpdateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardCreateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardTitleUpdateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardTypeUpdateRequest;
import com.server.bbo_gak.domain.card.dto.response.CardCreateResponse;
import com.server.bbo_gak.domain.card.dto.response.CardGetResponse;
import com.server.bbo_gak.domain.card.dto.response.CardListGetResponse;
import com.server.bbo_gak.domain.card.dto.response.CardTypeCountGetResponse;
import com.server.bbo_gak.domain.card.service.CardService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.annotation.AuthUser;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/cards/type-count")
    public ResponseEntity<CardTypeCountGetResponse> getCardTypeCounts(@AuthUser User user) {
        return ResponseEntity.ok(cardService.getCardTypeCountsInMyInfo(user));
    }

    @GetMapping("/cards/{card-id}")
    public ResponseEntity<CardGetResponse> getCardDetail(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId) {

        return ResponseEntity.ok(cardService.getCardDetail(user, cardId));
    }

    @GetMapping("/cards")
    public ResponseEntity<List<CardListGetResponse>> getCardList(
        @AuthUser User user,
        @RequestParam("type") String type,
        @RequestParam(value = "tag-ids", required = false) List<Long> tagIdList
    ) {

        return ResponseEntity.ok(cardService.getCardList(user, type, tagIdList));
    }

    @PostMapping("/card")
    public ResponseEntity<CardCreateResponse> createCard(
        @AuthUser User user,
        @RequestBody @Valid CardCreateRequest cardCreateRequest) {

        return ResponseEntity.ok(cardService.createCard(user, cardCreateRequest));
    }

    @PutMapping("/cards/{card-id}/card-type")
    public ResponseEntity<Void> updateCardType(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId,
        @RequestBody CardTypeUpdateRequest request
    ) {
        cardService.updateCardType(user, cardId, request);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/cards/{card-id}/title")
    public ResponseEntity<Void> updateCardTitle(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId,
        @RequestBody CardTitleUpdateRequest request
    ) {

        cardService.updateCardTitle(user, cardId, request);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/cards/{card-id}/content")
    public ResponseEntity<Void> updateCardContent(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId,
        @RequestBody CardContentUpdateRequest request
    ) {

        cardService.updateCardContent(user, cardId, request);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/cards/{card-id}")
    public ResponseEntity<Void> deleteCard(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId
    ) {

        cardService.deleteCard(user, cardId);
        return ResponseEntity.ok().body(null);
    }
}
