package com.server.bbo_gak.domain.card.controller;

import com.server.bbo_gak.domain.card.dto.response.CardSearchByTagListResponse;
import com.server.bbo_gak.domain.card.service.CardSearchService;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CardSearchController {

    private final CardSearchService cardSearchService;

    @GetMapping("/search/cards")
    public ResponseEntity<List<CardSearchByTagListResponse>> searchCardByTagList(
        @AuthUser User user,
        @RequestParam(value = "card-type-value-group", required = false) String cardTypeValueGroup,
        @RequestParam(value = "tag-ids") List<Long> tagIdList) {
        return ResponseEntity.ok(cardSearchService.searchCardByTagList(user, cardTypeValueGroup, tagIdList));
    }
}
