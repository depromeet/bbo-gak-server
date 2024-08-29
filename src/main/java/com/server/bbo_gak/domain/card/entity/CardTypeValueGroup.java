package com.server.bbo_gak.domain.card.entity;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardTypeValueGroup {

    MY_INFO("내_정보", new CardTypeValue[]{CardTypeValue.EXPERIENCE, CardTypeValue.INTERVIEW_QUESTION,
        CardTypeValue.PERSONAL_STATEMENT}),

    RECRUIT("공고", new CardTypeValue[]{CardTypeValue.ASSIGNMENT_PREPARING, CardTypeValue.DOCUMENT_PREPARING,
        CardTypeValue.INTERVIEW_PREPARING, CardTypeValue.COPY_FROM_MY_INFO}),

    RECRUIT_EXCEPT_COPIED("공고_복사본_제외",
        new CardTypeValue[]{CardTypeValue.ASSIGNMENT_PREPARING, CardTypeValue.DOCUMENT_PREPARING,
            CardTypeValue.INTERVIEW_PREPARING});


    private String value;
    private CardTypeValue[] cardTypeValueList;

    public static CardTypeValueGroup findByValue(String value) {
        return Arrays.stream(CardTypeValueGroup.values())
            .filter(cardType -> cardType.getValue().equals(value))
            .findFirst()
            .orElseThrow((() -> new NotFoundException(ErrorCode.CARD_TYPE_VALUE_GROUP_NOT_FOUND)));
    }

    public static CardTypeValueGroup findByCardTypeValue(CardTypeValue cardTypeValue) {
        return Arrays.stream(CardTypeValueGroup.values())
            .filter(cardTypeValueGroup -> cardTypeValueGroup.contains(cardTypeValue))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(ErrorCode.CARD_TYPE_VALUE_GROUP_NOT_FOUND));
    }

    public boolean contains(CardTypeValue cardTypeValue) {
        return Arrays.asList(cardTypeValueList).contains(cardTypeValue);
    }


}
