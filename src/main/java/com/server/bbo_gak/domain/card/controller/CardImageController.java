package com.server.bbo_gak.domain.card.controller;

import com.server.bbo_gak.domain.card.dto.request.CardImageDeleteRequest;
import com.server.bbo_gak.domain.card.dto.request.CardImageUploadCompleteRequest;
import com.server.bbo_gak.domain.card.dto.response.CardImageUploadCompleteResponse;
import com.server.bbo_gak.domain.card.service.CardImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/card-images")
@RequiredArgsConstructor
public class CardImageController {

    private final CardImageService cardImageService;

    @PostMapping("/static-urls/{cardId}")
    public ResponseEntity<List<CardImageUploadCompleteResponse>> completeCardImageUpload(
        @PathVariable("cardId") Long cardId,
        @RequestBody List<CardImageUploadCompleteRequest> request
    ) {
        return ResponseEntity.ok(cardImageService.addImagesToCard(cardId, request));
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteCardImage(@RequestBody CardImageDeleteRequest request) {
        cardImageService.deleteCardImage(request);
        return ResponseEntity.noContent().build();
    }
}
