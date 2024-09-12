package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.CardTagSearchHistory;
import com.server.bbo_gak.domain.card.entity.Tag;
import com.server.bbo_gak.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTagSearchHistoryRepository extends JpaRepository<CardTagSearchHistory, Long> {

    List<CardTagSearchHistory> findTop10ByUserOrderByCreatedDate(User user);

    void deleteAllByUserAndTagIn(User user, List<Tag> tags);
}
