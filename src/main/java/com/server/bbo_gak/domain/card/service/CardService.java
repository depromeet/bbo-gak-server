package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dao.CardDao;
import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dao.CardTagRepository;
import com.server.bbo_gak.domain.card.dao.CardTypeRepository;
import com.server.bbo_gak.domain.card.dao.TagRepository;
import com.server.bbo_gak.domain.card.dto.request.CardContentUpdateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardCreateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardTitleUpdateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardTypeUpdateRequest;
import com.server.bbo_gak.domain.card.dto.response.CardCreateResponse;
import com.server.bbo_gak.domain.card.dto.response.CardGetResponse;
import com.server.bbo_gak.domain.card.dto.response.CardListGetResponse;
import com.server.bbo_gak.domain.card.dto.response.CardTypeCountGetResponse;
import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTag;
import com.server.bbo_gak.domain.card.entity.CardType;
import com.server.bbo_gak.domain.card.entity.CardTypeValue;
import com.server.bbo_gak.domain.card.entity.CardTypeValueGroup;
import com.server.bbo_gak.domain.card.entity.Tag;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.InvalidValueException;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardDao cardDao;
    private final CardRepository cardRepository;
    private final CardTagRepository cardTagRepository;
    private final TagRepository tagRepository;
    private final CardTypeRepository cardTypeRepository;

    @Transactional(readOnly = true)
    public CardTypeCountGetResponse getCardTypeCountsInMyInfo(User user) {

        CardTypeValue[] cardTypeValueList = CardTypeValueGroup.MY_INFO.getCardTypeValueList();

        List<Card> cards = cardDao.findAllByUserIdAndCardTypeValueList(user, cardTypeValueList, false);

        return CardTypeCountGetResponse.from(cards);
    }


    @Transactional(readOnly = true)
    public CardGetResponse getCardDetail(User user, Long cardId) {

        Card card = cardRepository.findByIdAndUser(cardId, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        List<CardTag> cardTags = cardTagRepository.findAllByCard(card);

        return CardGetResponse.of(card, cardTags);
    }

    @Transactional(readOnly = true)
    public List<CardListGetResponse> getCardList(User user, String cardTypeValue, List<Long> tagIdList) {

        List<Tag> tagList = tagRepository.findAllById(Optional.ofNullable(tagIdList).orElse(Collections.emptyList()));

        List<Card> cards = cardDao.findAllByUserIdAndCardTypeValue(user, CardTypeValue.findByValue(cardTypeValue),
            null);

        return cards.stream()
            .filter(card -> {

                if (tagList.isEmpty()) {
                    return true;
                }
                
                return card.isTagListContain(tagList);
            })
            .sorted(Comparator.comparing(Card::getUpdatedDate).reversed())
            .map(card -> CardListGetResponse.of(card, card.getCardTagList()))
            .collect(Collectors.toList());
    }


    @Transactional
    public CardCreateResponse createCard(User user, CardCreateRequest cardCreateRequest) {

        Card card = cardRepository.save(Card.creatEmptyCard(user));

        List<CardType> cardTypeList = getValidCardTypeList(cardCreateRequest.cardTypeValueGroup(), card,
            cardCreateRequest.cardTypeValueList());

        cardTypeRepository.saveAll(cardTypeList);

        List<CardTag> cardTagList = cardCreateRequest.tagIdList().stream()
            .map(tagId -> tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND)))
            .map(tag -> new CardTag(card, tag))
            .toList();

        cardTagRepository.saveAll(cardTagList);

        return new CardCreateResponse(card.getId());
    }

    @Transactional
    public void updateCardType(User user, Long cardId, CardTypeUpdateRequest request) {

        Card card = cardRepository.findByIdAndUser(cardId, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        cardTypeRepository.deleteAll(card.getCardTypeList());

        List<CardType> cardTypeList = getValidCardTypeList(request.cardTypeValueGroup(), card,
            request.cardTypeValueList());

        cardTypeRepository.saveAll(cardTypeList);

        // 양방향 연관 관계 고려 메소드
        card.updateCardTypeList(cardTypeList);
    }


    @Transactional
    public void updateCardTitle(User user, Long cardId, CardTitleUpdateRequest request) {

        Card card = cardRepository.findByIdAndUser(cardId, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        card.updateTitle(request.title());
    }

    @Transactional
    public void updateCardContent(User user, Long cardId, CardContentUpdateRequest request) {

        Card card = cardRepository.findByIdAndUser(cardId, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        card.updateContent(request.content());
    }

    @Transactional
    public void deleteCard(User user, Long cardId) {

        Card card = cardRepository.findByIdAndUser(cardId, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        cardTagRepository.deleteAll(card.getCardTagList());
        cardRepository.delete(card);
    }

    private List<CardType> getValidCardTypeList(String cardTypeValueGroupValue, Card card,
        List<String> cardTypeValueList) {

        CardTypeValueGroup cardTypeValueGroup = CardTypeValueGroup.findByValue(cardTypeValueGroupValue);

        List<CardType> cardTypeList = cardTypeValueList.stream()
            .map(cardTypeValue -> new CardType(card, CardTypeValue.findByValue(cardTypeValue)))
            .toList();

        // 내정보에서 카드 생성인 경우
        if (cardTypeValueGroup.equals(CardTypeValueGroup.MY_INFO)) {

            if (cardTypeList.size() > 1) {
                throw new InvalidValueException(ErrorCode.MY_INFO_CARD_TYPE_OVERSIZE);
            }

            if (!CardTypeValueGroup.MY_INFO.contains(cardTypeList.getFirst().getCardTypeValue())) {
                throw new InvalidValueException(ErrorCode.CARD_TYPE_NOT_MATCHED);
            }

            return cardTypeList;
        }

        // 공고에서 카드 생성인 경우
        if (cardTypeValueGroup.equals(CardTypeValueGroup.RECRUIT)) {

            for (CardType cardType : cardTypeList) {
                if (!CardTypeValueGroup.RECRUIT.contains(cardType.getCardTypeValue())) {
                    throw new InvalidValueException(ErrorCode.CARD_TYPE_NOT_MATCHED);
                }
            }

            return cardTypeList;
        }

        throw new InvalidValueException(ErrorCode.CARD_TYPE_NOT_FOUND);
    }
}
