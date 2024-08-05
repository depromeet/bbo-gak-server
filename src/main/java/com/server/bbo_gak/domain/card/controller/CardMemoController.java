package com.server.bbo_gak.domain.card.controller;

import com.server.bbo_gak.domain.card.dto.request.CardMemoContentUpdateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardMemoCreateRequest;
import com.server.bbo_gak.domain.card.dto.response.CardMemoCreateResponse;
import com.server.bbo_gak.domain.card.dto.response.CardMemoGetResponse;
import com.server.bbo_gak.domain.card.service.CardMemoService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.annotation.AuthUser;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardMemoController {

    private final CardMemoService cardMemoService;

    @GetMapping("/{card-id}/card-memo")
    public ResponseEntity<List<CardMemoGetResponse>> getCardMemoList(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId) {

        return ResponseEntity.ok(cardMemoService.getCardMemoList(user, cardId));
    }

    @PostMapping("/{card-id}/card-memo")
    public ResponseEntity<CardMemoCreateResponse> createCardMemo(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId,
        @RequestBody CardMemoCreateRequest request) {

        return ResponseEntity.ok(cardMemoService.createCardMemo(user, request, cardId));
    }

    @PutMapping("/{card-id}/card-momo/{card-memo-id}/content")
    public ResponseEntity<Void> updateCardMemoContent(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId,
        @PathVariable("card-memo-id") Long cardMemoId,
        @RequestBody CardMemoContentUpdateRequest request) {

        cardMemoService.updateCardMemo(user, request, cardId, cardMemoId);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/{card-id}/card-momo/{card-memo-id}")
    public ResponseEntity<Void> deleteCardMemo(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId,
        @PathVariable("card-memo-id") Long cardMemoId) {

        cardMemoService.deleteCardMemo(user, cardId, cardMemoId);
        return ResponseEntity.ok().body(null);
    }
}
