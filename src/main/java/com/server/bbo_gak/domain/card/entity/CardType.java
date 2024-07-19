package com.server.bbo_gak.domain.card.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardType {

    EXPERIENCE("경험 정리"),
    PERSONAL_STATEMENT("자기소개서"),
    INTERVIEW_QUESTION("면접 질문");

    private final String value;
}
