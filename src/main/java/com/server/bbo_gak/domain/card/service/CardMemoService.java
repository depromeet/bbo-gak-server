package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.user.entity.User;

public interface CardMemoService {

    void createCardMemo();

    void updateCardMemo();

    void deleteCardMemo();

    void getCardMemoList(User user, Long cardId);

}
