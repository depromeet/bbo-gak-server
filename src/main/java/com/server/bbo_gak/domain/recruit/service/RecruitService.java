package com.server.bbo_gak.domain.recruit.service;

import com.server.bbo_gak.domain.card.entity.CardType;
import com.server.bbo_gak.domain.user.entity.User;

public interface RecruitService {

    void getTotalRecruitList(); //생성순 정렬

    void getProgressRecruitList();

    void createRecruit();

    void deleteRecruit();

    void updateRecruit();

    void getRecruitDetail();

    void getCardListInRecruit(User user, Long recruitId, CardType type);

    void getCardTypeCountsInRecruit(User user, Long recruitId);

    void copyMyInfoCardToRecruit(User user, Long CardId, Long recruitId);
}
