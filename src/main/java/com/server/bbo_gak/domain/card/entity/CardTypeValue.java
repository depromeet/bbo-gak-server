package com.server.bbo_gak.domain.card.entity;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardTypeValue {

    EXPERIENCE("경험_정리"),
    PERSONAL_STATEMENT("자기소개서"),
    INTERVIEW_QUESTION("면접_질문"),
    DOCUMENT_PREPARING("서류_준비"),
    ASSIGNMENT_PREPARING("과제_준비"),
    INTERVIEW_PREPARING("인터뷰_준비"),
    COPY_FROM_MY_INFO("내_정보_복사");

    private final String value;

    public static CardTypeValue findByValue(String value) {
        return Arrays.stream(CardTypeValue.values())
            .filter(cardType -> cardType.getValue().equals(value))
            .findFirst()
            .orElseThrow((() -> new NotFoundException(ErrorCode.CARD_TYPE_NOT_FOUND)));
    }
}
