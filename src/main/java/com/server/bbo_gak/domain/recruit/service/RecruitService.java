package com.server.bbo_gak.domain.recruit.service;

import com.server.bbo_gak.domain.card.entity.CardType;
import com.server.bbo_gak.domain.recruit.dao.RecruitRepository;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitScheduleCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetDetailResponse;
import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
import com.server.bbo_gak.domain.recruit.entity.RecruitStatus;
import com.server.bbo_gak.domain.recruit.entity.RecruitStatusCategory;
import com.server.bbo_gak.domain.recruit.entity.Season;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final SeasonService seasonService;
    private final RecruitScheduleService recruitScheduleService;

    public List<RecruitGetDetailResponse> getTotalRecruitList(User user) {
        List<Recruit> recruits = recruitRepository.findAllByUserId(user.getId());

        return recruits.stream()
            .sorted((r1, r2) -> r2.getCreatedDate().compareTo(r1.getCreatedDate()))
            .map(RecruitGetDetailResponse::from)
            .toList();
    }

    public List<RecruitGetDetailResponse> getRecruitListBySeason(User user, String seasonName) {
        Season season = seasonService.getSeasonByName(seasonName);
        return recruitRepository.findAllByUserIdAndSeason(user.getId(), season).stream()
            .map(RecruitGetDetailResponse::from)
            .toList();
    }


    // TODO: 진행중인 공고는 일정등록이 안된 것을 우선으로 그 이후에는 RecruitSchedule이 현재와 가까운 순으로 정렬한다.
    public List<RecruitGetDetailResponse> getProgressingRecruitList(User user) {

        List<Recruit> recruits = recruitRepository.findAllByUserId(user.getId());

        // 불합격이 아닌 공고중에서 스케줄이 비어 있거나 지난 일정만 등록되어 있는 공고 중에서 지원 상태가를 분리해냄
        Map<Boolean, List<Recruit>> partitionedRecruits = partitionRecruits(recruits);

        List<Recruit> matchingRecruits = partitionedRecruits.get(true);
        List<Recruit> nonMatchingRecruits = partitionedRecruits.get(false);

        //nonMatchingRecruits에서 시간이 지나지 않은 스케줄 중에서 가장 현재와 가까운 걸 기준으로 정렬
        List<Recruit> sortedNonMatchingRecruits = nonMatchingRecruits.stream()
            .sorted((r1, r2) -> {
                LocalDate nearestDate1 = r1.getScheduleList().stream()
                    .map(RecruitSchedule::getDeadLine)
                    .filter(deadLine -> deadLine.isAfter(LocalDate.now()))
                    .min(Comparator.naturalOrder())
                    .orElse(LocalDate.MAX);

                LocalDate nearestDate2 = r2.getScheduleList().stream()
                    .map(RecruitSchedule::getDeadLine)
                    .filter(deadLine -> deadLine.isAfter(LocalDate.now()))
                    .min(Comparator.naturalOrder())
                    .orElse(LocalDate.MAX);

                return nearestDate1.compareTo(nearestDate2);
            })
            .toList();

        matchingRecruits.addAll(sortedNonMatchingRecruits);

        return matchingRecruits.stream()
            .map(RecruitGetDetailResponse::from)
            .toList();
    }

    private Map<Boolean, List<Recruit>> partitionRecruits(List<Recruit> recruits) {
        return recruits.stream()
            .filter(recruit -> !RecruitStatusCategory.isRejectionStatus(recruit.getRecruitStatus())) // 불합격 상태 필터링
            .collect(Collectors.partitioningBy(recruit ->
                recruit.getScheduleList().isEmpty() ||
                    recruit.getScheduleList().stream()
                        .allMatch(schedule -> schedule.getDeadLine().isBefore(LocalDate.now()))
            ));
    }

    @Transactional
    public Long createRecruit(User user, RecruitCreateRequest request) {

        RecruitSchedule recruitSchedule = recruitScheduleService.createRecruitSchedule(
            RecruitScheduleCreateRequest.of(request.recruitScheduleStage(), request.deadline())
        );
        Season season = seasonService.getSeasonByName(request.season());
        Recruit recruit = request.toEntity(user, season, recruitSchedule);
        return recruitRepository.save(recruit).getId();
    }

    @Transactional
    public void deleteRecruit(User user, Long recruitId) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);

        recruitRepository.deleteById(recruitId);
    }

    @Transactional
    public RecruitGetDetailResponse updateRecruitTitle(User user, Long recruitId, String title) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);

        recruit.updateTitle(title);

        return RecruitGetDetailResponse.from(recruitRepository.save(recruit));
    }

    @Transactional
    public RecruitGetDetailResponse updateRecruitSeason(User user, Long recruitId, Season season) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);

        recruit.updateSeason(season);

        return RecruitGetDetailResponse.from(recruitRepository.save(recruit));
    }

    @Transactional
    public RecruitGetDetailResponse updateRecruitStatus(User user, Long recruitId, RecruitStatus recruitStatus) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);

        recruit.updateRecruitStatus(recruitStatus);

        return RecruitGetDetailResponse.from(recruitRepository.save(recruit));
    }

    @Transactional
    public RecruitGetDetailResponse updateRecruitSiteUrl(User user, Long recruitId, String siteUrl) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);

        recruit.updateSiteUrl(siteUrl);

        return RecruitGetDetailResponse.from(recruitRepository.save(recruit));
    }

    public RecruitGetDetailResponse getRecruitDetail(User user, Long recruitId) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);

        return RecruitGetDetailResponse.from(recruit);
    }

    private Recruit findRecruitByUserAndId(User user, Long recruitId) {
        return recruitRepository.findByUserIdAndId(user.getId(), recruitId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.RECRUIT_NOT_FOUND));
    }

    public void getCardListInRecruit(User user, Long recruitId, CardType type) {

    }

    public void getCardTypeCountsInRecruit(User user, Long recruitId) {

    }

    public void copyMyInfoCardToRecruit(User user, Long CardId, Long recruitId) {

    }
}
