package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dao.CardTagRepository;
import com.server.bbo_gak.domain.card.dao.TagRepository;
import com.server.bbo_gak.domain.card.dto.request.CardContentUpdateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardTitleUpdateRequest;
import com.server.bbo_gak.domain.card.dto.response.CardCreateResponse;
import com.server.bbo_gak.domain.card.dto.response.CardGetResponse;
import com.server.bbo_gak.domain.card.dto.response.CardGetsResponse;
import com.server.bbo_gak.domain.card.dto.response.CardTypeCountGetResponse;
import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTag;
import com.server.bbo_gak.domain.card.entity.CardType;
import com.server.bbo_gak.domain.card.entity.Tag;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardTagRepository cardTagRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
    public CardTypeCountGetResponse getCardTypeCounts(User user) {

        List<Card> cards = cardRepository.findAllByUserId(user.getId());

        return CardTypeCountGetResponse.of(cards);
    }

    @Override
    @Transactional(readOnly = true)
    public CardGetResponse getCardDetail(User user, Long cardId) {

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        List<CardTag> cardTags = cardTagRepository.findAllByCard(card);

        return CardGetResponse.of(card, cardTags);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardGetsResponse> getCardList(User user, String type) {

        List<Card> cards = cardRepository.findAllByUserIdAndCardType(user.getId(), CardType.findByValue(type));

        return cards.stream()
            .map(card -> CardGetsResponse.of(card, card.getCardTagList()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CardCreateResponse createCard(User user, String type) {

        Card card = cardRepository.save(Card.creatEmptyCard(type, user.getId()));

        return CardCreateResponse.of(card.getId(), card.getCardType().getValue());
    }

    @Override
    @Transactional
    public void addCardTag(User user, Long cardId, Long tagId) {

        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND));

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        validateTagDuplicated(tag.getId(), card);

        cardTagRepository.save(new CardTag(card, tag));
    }

    @Override
    @Transactional
    public void updateCardTitle(User user, Long cardId, CardTitleUpdateRequest request) {

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        card.updateTitle(request.title());
    }

    @Override
    @Transactional
    public void updateCardContent(User user, Long cardId, CardContentUpdateRequest request) {

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        card.updateContent(request.content());
    }

    @Override
    @Transactional
    public void deleteCard(User user, Long cardId) {

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        cardTagRepository.deleteAll(card.getCardTagList());
        cardRepository.delete(card);
    }

    @Override
    @Transactional
    public void deleteCardTag(User user, Long cardId, Long tagId) {

        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND));

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        CardTag cardTag = cardTagRepository.findByCardAndTag(card, tag)
            .orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND));

        cardTagRepository.delete(cardTag);
    }


    private void validateTagDuplicated(Long tagId, Card card) {
        for (CardTag cardTag : card.getCardTagList()) {
            if (cardTag.getTag().getId().equals(tagId)) {
                throw new BusinessException(ErrorCode.TAG_DUPLICATED);
            }
        }
    }
}
