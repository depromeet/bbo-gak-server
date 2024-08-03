package com.server.bbo_gak.domain.recruit.service;

import com.server.bbo_gak.domain.card.entity.CardType;
import com.server.bbo_gak.domain.recruit.dao.RecruitRepository;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetResponse;
import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.recruit.entity.Season;
import com.server.bbo_gak.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final SeasonService seasonService;

    public List<RecruitGetResponse> getTotalRecruitList(User user) {
        List<Recruit> recruits = recruitRepository.findAllByUserId(user.getId());

        return recruits.stream()
            .sorted((r1, r2) -> r2.getCreatedDate().compareTo(r1.getCreatedDate()))
            .map(RecruitGetResponse::from)
            .toList();
    }

    public List<RecruitGetResponse> getRecruitListBySeason(User user, String seasonName) {
        Season season = seasonService.getSeasonByName(seasonName);
        return recruitRepository.findAllByUserIdAndSeason(user.getId(), season).stream()
            .map(RecruitGetResponse::from)
            .toList();
    }

    public List<RecruitGetResponse> getProgressRecruitList() {
        
    }

    void createRecruit() {

    }

    void deleteRecruit() {

    }

    void updateRecruit() {

    }

    void getRecruitDetail() {

    }

    void getCardListInRecruit(User user, Long recruitId, CardType type) {

    }

    void getCardTypeCountsInRecruit(User user, Long recruitId) {

    }

    void copyMyInfoCardToRecruit(User user, Long CardId, Long recruitId) {

    }
}
