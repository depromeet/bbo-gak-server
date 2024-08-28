package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dao.CardTypeRepository;
import com.server.bbo_gak.domain.card.dto.request.CardTypeUpdateRequest;
import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardType;
import com.server.bbo_gak.domain.card.entity.CardTypeValue;
import com.server.bbo_gak.domain.card.entity.CardTypeValueGroup;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.InvalidValueException;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardTypeService {

    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;


    @Transactional(readOnly = true)
    public List<CardType> getValidCardTypeList(CardTypeValueGroup cardTypeValueGroup, Card card,
        List<String> cardTypeValueList) {

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

    @Transactional
    public void updateCardType(User user, Long cardId, CardTypeUpdateRequest request) {

        CardTypeValueGroup cardTypeValueGroup = CardTypeValueGroup.findByValue(request.cardTypeValueGroup());

        Card card = cardRepository.findByIdAndUser(cardId, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        cardTypeRepository.deleteAll(card.getCardTypeList());

        List<CardType> cardTypeList = getValidCardTypeList(cardTypeValueGroup, card, request.cardTypeValueList());

        cardTypeRepository.saveAll(cardTypeList);

        // 양방향 연관 관계 고려 메소드
        card.updateCardTypeList(cardTypeList);
    }
}
