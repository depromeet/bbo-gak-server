package com.server.bbo_gak.domain.recruit.service;


import com.server.bbo_gak.domain.recruit.dao.SeasonRepository;
import com.server.bbo_gak.domain.recruit.dto.response.SeasonGetResponse;
import com.server.bbo_gak.domain.recruit.entity.Season;
import com.server.bbo_gak.domain.recruit.entity.SeasonPeriod;
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

    public Season getSeasonByName(User user, String name) {
        return seasonRepository.findByUserIdAndName(user.getId(), name)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SEASON_NOT_FOUND));
    }

    private String getCurrentSeason() {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();

        SeasonPeriod currentPeriod = SeasonPeriod.fromMonth(month);
        return currentPeriod.getSeasonName(year);
    }

    private List<Season> generateDefaultSeasonByCurrentTime(User user) {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();

        List<Season> periods = new ArrayList<>();

        SeasonPeriod currentPeriod = SeasonPeriod.fromMonth(month);
        periods.add(new Season(currentPeriod.getSeasonName(year), user));

        SeasonPeriod nextPeriod = (currentPeriod == SeasonPeriod.FIRST_HALF)
            ? SeasonPeriod.SECOND_HALF
            : SeasonPeriod.FIRST_HALF;

        int nextPeriodYear = (currentPeriod == SeasonPeriod.FIRST_HALF) ? year : year + 1;
        periods.add(new Season(nextPeriod.getSeasonName(nextPeriodYear), user));

        return periods;
    }
}
