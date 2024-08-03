package com.server.bbo_gak.domain.recruit.service;


import com.server.bbo_gak.domain.recruit.dao.SeasonRepository;
import com.server.bbo_gak.domain.recruit.dto.response.SeasonGetResponse;
import com.server.bbo_gak.domain.recruit.entity.Season;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonRepository seasonRepository;

    @Transactional
    public List<SeasonGetResponse> getSeasonListByUser(User user) {
        boolean existsCurrentSeason = seasonRepository.existsByUserIdAndName(user.getId(), getCurrentSeason());

        if (!existsCurrentSeason) {
            seasonRepository.saveAll(generateDefaultSeasonByCurrentTime(user));
        }
        return seasonRepository.findAllByUserId(user.getId()).stream()
            .map(SeasonGetResponse::from)
            .toList();
    }

    public Season getSeasonByName(String name) {
        return seasonRepository.findByName(name)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SEASON_NOT_FOUND));
    }

    private String getCurrentSeason() {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();

        String halfYear = month <= 6 ? "상반기" : "하반기";

        return year + " " + halfYear;
    }

    private List<Season> generateDefaultSeasonByCurrentTime(User user) {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();

        List<Season> periods = new ArrayList<>();

        if (month >= 1 && month <= 6) {
            periods.add(new Season(year + " 상반기", user));
            periods.add(new Season(year + " 하반기", user));
        } else {
            periods.add(new Season(year + " 하반기", user));
            periods.add(new Season((year + 1) + " 상반기", user));
        }

        return periods;
    }
}
