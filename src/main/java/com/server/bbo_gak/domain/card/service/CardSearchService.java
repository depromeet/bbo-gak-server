package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dao.CardDao;
import com.server.bbo_gak.domain.card.dao.TagRepository;
import com.server.bbo_gak.domain.card.dto.response.CardSearchByTagListResponse;
import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTypeValue;
import com.server.bbo_gak.domain.card.entity.CardTypeValueGroup;
import com.server.bbo_gak.domain.card.entity.Tag;
import com.server.bbo_gak.domain.user.entity.User;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardSearchService {

    private final TagRepository tagRepository;
    private final CardDao cardDao;

    @Transactional(readOnly = true)
    public List<CardSearchByTagListResponse> searchCardByTagList(User user, String cardTypeValueGroup,
        List<Long> tagIdList) {

        List<Tag> tagList = tagRepository.findAllById(tagIdList);

        List<Card> cards = cardDao.findAllByUserIdAndCardTypeValueList(user, getCardTypeValueList(cardTypeValueGroup),
            null);

        // TODO 필터링 로직 디비로 가게 하기
        return cards.stream()
            .filter(card -> tagList.isEmpty() || card.isTagListContain(tagList))
            .sorted(Comparator.comparing(Card::getUpdatedDate).reversed())
            .map(CardSearchByTagListResponse::from)
            .collect(Collectors.toList());
    }

    private CardTypeValue[] getCardTypeValueList(String cardTypeValueGroupInput) {
        return Optional.ofNullable(cardTypeValueGroupInput)
            .map(cardTypeValueGroup -> CardTypeValueGroup.findByValue(cardTypeValueGroup).getCardTypeValueList())
            .orElseGet(() -> new CardTypeValue[0]);
    }

}
