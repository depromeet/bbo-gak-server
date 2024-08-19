package com.server.bbo_gak.domain.card.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardTypeValueGroup {

    MY_INFO(new CardTypeValue[]{CardTypeValue.EXPERIENCE, CardTypeValue.INTERVIEW_QUESTION,
        CardTypeValue.PERSONAL_STATEMENT}),

    RECRUIT(new CardTypeValue[]{CardTypeValue.ASSIGNMENT_PREPARING, CardTypeValue.DOCUMENT_PREPARING,
        CardTypeValue.INTERVIEW_PREPARING});
    
    private CardTypeValue[] cardTypeValueList;
}
