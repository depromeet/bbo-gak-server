package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dto.request.CardContentUpdateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardTitleUpdateRequest;
import com.server.bbo_gak.domain.card.dto.response.CardCreateResponse;
import com.server.bbo_gak.domain.card.dto.response.CardGetResponse;
import com.server.bbo_gak.domain.card.dto.response.CardGetsResponse;
import com.server.bbo_gak.domain.card.dto.response.CardTypeCountGetResponse;
import com.server.bbo_gak.domain.user.entity.User;
import java.util.List;

public interface CardService {

    CardTypeCountGetResponse getCardTypeCounts(User user);

    CardGetResponse getCardDetail(User user, Long cardId);

    List<CardGetsResponse> getCardList(User user, String type);

    CardCreateResponse createCard(User user, String type);

    void addCardTag(User user, Long cardId, Long tagId);

    void deleteCardTag(User user, Long cardId, Long tagId);

    void updateCardTitle(User user, Long cardId, CardTitleUpdateRequest request);

    void updateCardContent(User user, Long cardId, CardContentUpdateRequest request);

    void deleteCard(User user, Long cardId);
}
