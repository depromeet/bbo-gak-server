package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dao.CardMemoRepository;
import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dto.request.CardMemoContentUpdateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardMemoCreateRequest;
import com.server.bbo_gak.domain.card.dto.response.CardMemoCreateResponse;
import com.server.bbo_gak.domain.card.dto.response.CardMemoGetResponse;
import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardMemo;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardMemoService {

    private final CardRepository cardRepository;

    private final CardMemoRepository cardMemoRepository;


    public List<CardMemoGetResponse> getCardMemoList(User user, Long cardId) {

        Card card = cardRepository.findByIdAndUser(cardId, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        return card.getCardMemoList().stream()
            .map(CardMemoGetResponse::of)
            .toList();
    }

    public CardMemoCreateResponse createCardMemo(User user, CardMemoCreateRequest request, Long cardId) {

        Card card = cardRepository.findByIdAndUser(cardId, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        CardMemo cardMemo = cardMemoRepository.save(new CardMemo(card, request.content()));

        return new CardMemoCreateResponse(cardMemo.getId(), cardMemo.getContent());
    }

    public void updateCardMemo(User user, CardMemoContentUpdateRequest request, Long cardId, Long cardMemoId) {

        CardMemo cardMemo = findCardMemo(user, cardId, cardMemoId);

        cardMemo.updateContent(request.content());

        cardMemoRepository.save(cardMemo);
    }

    public void deleteCardMemo(User user, Long cardId, Long cardMemoId) {

        CardMemo cardMemo = findCardMemo(user, cardId, cardMemoId);

        cardMemoRepository.delete(cardMemo);
    }

    private CardMemo findCardMemo(User user, Long cardId, Long cardMemoId) {

        Card card = cardRepository.findByIdAndUser(cardId, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        return cardMemoRepository.findByIdAndCard(cardMemoId, card)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_MEMO_NOT_FOUND));
    }
}
