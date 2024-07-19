package com.server.bbo_gak.domain.card.service;

public interface CardService {

    void getCardTypeCounts();

    void getCardDetail();

    void createCard();

    void getCardList();
    
    // TODO 저장 정책 상의 해봐야함.
    void updateCardTitle();

    void updateCardBody();

    void updateCardType();

    void updateTag();

    void deleteCard();


}
