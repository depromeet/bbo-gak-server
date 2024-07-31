package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dao.CardTagRepository;
import com.server.bbo_gak.domain.card.dao.TagRepository;
import com.server.bbo_gak.domain.card.dto.response.TagGetResponse;
import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.card.entity.CardTag;
import com.server.bbo_gak.domain.card.entity.Tag;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final CardRepository cardRepository;
    private final TagRepository tagRepository;
    private final CardTagRepository cardTagRepository;

    public List<TagGetResponse> getAllTagList(User user, Long cardId) {

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        // 이미 할당된 태그 제외하기 위해 할당된 태그 id 리스트 get
        List<Long> assignedTagIdList = card.getCardTagList().stream()
            .map(cardTag -> cardTag.getTag().getId())
            .toList();

        return tagRepository.findAllByIdIsNotIn(assignedTagIdList).stream()
            .map(TagGetResponse::of)
            .toList();
    }

    @Transactional
    public void addCardTag(User user, Long cardId, Long tagId) {

        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND));

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_NOT_FOUND));

        validateTagDuplicated(tag.getId(), card);

        cardTagRepository.save(new CardTag(card, tag));
    }

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
