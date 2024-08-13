package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dao.CardImageRepository;
import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dto.request.CardImageDeleteRequest;
import com.server.bbo_gak.domain.card.dto.request.CardImageUploadCompleteRequest;
import com.server.bbo_gak.domain.card.dto.response.CardImageUploadCompleteResponse;
import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardImage;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import com.server.bbo_gak.global.utils.s3.S3Util;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class CardImageServiceImpl implements CardImageService {

    private final CardRepository cardRepository;
    private final CardImageRepository cardImageRepository;
    private final S3Util s3Util;

    @Override
    @Transactional
    public List<CardImageUploadCompleteResponse> addImagesToCard(Long cardId,
        List<CardImageUploadCompleteRequest> requests) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        List<CardImage> cardImageList = requests.stream()
            .map(request -> CardImage.of(card, request.fileName()))
            .toList();

        cardImageRepository.saveAll(cardImageList);

        return requests.stream()
            .map(request -> s3Util.getS3ObjectUrl(request.fileName()))
            .map(CardImageUploadCompleteResponse::new)
            .toList();
    }

    @Override
    @Transactional
    public void deleteCardImage(CardImageDeleteRequest request) {
        Card card = cardRepository.findById(request.cardId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        cardImageRepository.deleteByCardAndImageUrl(card, request.staticUrl());
    }
}
