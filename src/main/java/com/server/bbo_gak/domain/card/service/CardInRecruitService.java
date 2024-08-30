package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dao.CardCopyInfoRepository;
import com.server.bbo_gak.domain.card.dao.CardDao;
import com.server.bbo_gak.domain.card.dao.CardImageRepository;
import com.server.bbo_gak.domain.card.dao.CardMemoRepository;
import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dao.CardTagRepository;
import com.server.bbo_gak.domain.card.dao.CardTypeRepository;
import com.server.bbo_gak.domain.card.dao.TagRepository;
import com.server.bbo_gak.domain.card.dto.response.CardCreateResponse;
import com.server.bbo_gak.domain.card.dto.response.CardListGetResponse;
import com.server.bbo_gak.domain.card.dto.response.CardTypeCountInRecruitGetResponse;
import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardCopyInfo;
import com.server.bbo_gak.domain.card.entity.CardImage;
import com.server.bbo_gak.domain.card.entity.CardMemo;
import com.server.bbo_gak.domain.card.entity.CardTag;
import com.server.bbo_gak.domain.card.entity.CardType;
import com.server.bbo_gak.domain.card.entity.CardTypeValue;
import com.server.bbo_gak.domain.card.entity.CardTypeValueGroup;
import com.server.bbo_gak.domain.card.entity.Tag;
import com.server.bbo_gak.domain.recruit.dao.RecruitRepository;
import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardInRecruitService {

    private final CardDao cardDao;
    private final CardRepository cardRepository;
    private final RecruitRepository recruitRepository;
    private final CardMemoRepository cardMemoRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CardTagRepository cardTagRepository;
    private final CardImageRepository cardImageRepository;
    private final CardCopyInfoRepository cardCopyInfoRepository;
    private final TagRepository tagRepository;

    private final CardTypeService cardTypeService;

    @Transactional(readOnly = true)
    public CardTypeCountInRecruitGetResponse getCardTypeCountsInRecruit(User user, Long recruitId) {

        CardTypeValue[] cardTypeValueList = CardTypeValueGroup.RECRUIT.getCardTypeValueList();

        List<Card> cards = cardDao.findAllByUserIdAndCardTypeValueList(user, cardTypeValueList, recruitId);

        return CardTypeCountInRecruitGetResponse.from(cards);
    }

    @Transactional(readOnly = true)
    public List<CardListGetResponse> getCardListInRecruit(User user, Long recruitId, String cardTypeValue
        , List<Long> tagIdList) {

        List<Tag> tagList = tagRepository.findAllById(Optional.ofNullable(tagIdList).orElse(Collections.emptyList()));

        List<Card> cards = cardDao.findAllByUserIdAndCardTypeValue(user, CardTypeValue.findByValue(cardTypeValue),
            recruitId);

        return cards.stream()
            .filter(card -> tagList.isEmpty() || card.isTagListContain(tagList))
            .sorted(Comparator.comparing(Card::getUpdatedDate).reversed())
            .map(card -> CardListGetResponse.of(card, card.getCardTagList()))
            .collect(Collectors.toList());
    }

    @Transactional
    public CardCreateResponse copyCardFromMyInfo(User user, Long cardId, Long recruitId) {

        Card card = cardRepository.findByIdAndUser(cardId, user)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        Recruit recruit = recruitRepository.findByUserIdAndId(user.getId(), recruitId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.RECRUIT_NOT_FOUND));

        Card copiedCard = Card.copyCardFromMyInfo(card, user, recruit);

        cardRepository.save(copiedCard);

        copyCardDataListFromCard(card, copiedCard);

        return new CardCreateResponse(copiedCard.getId());
    }

    private void copyCardDataListFromCard(Card card, Card copiedCard) {

        saveDataList(card.getCardMemoList(), cardMemoRepository,
            cardMemo -> new CardMemo(copiedCard, cardMemo.getContent()));

        saveDataList(card.getCardTagList(), cardTagRepository,
            cardTag -> new CardTag(copiedCard, cardTag.getTag()));

        saveDataList(card.getCardImageList(), cardImageRepository,
            cardImage -> CardImage.of(copiedCard, cardImage.getFileName()));

        cardTypeRepository.save(new CardType(copiedCard, CardTypeValue.COPY_FROM_MY_INFO));
        cardCopyInfoRepository.save(new CardCopyInfo(copiedCard));
    }

    private <T, R> void saveDataList(List<T> items, JpaRepository<R, ?> repository, Function<T, R> mapper) {

        List<R> copiedList = items.stream()
            .map(mapper)
            .toList();

        repository.saveAll(copiedList);
    }
}
