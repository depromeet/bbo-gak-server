package com.server.bbo_gak.domain.recruit.service;

import com.server.bbo_gak.domain.recruit.dao.RecruitRepository;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitScheduleCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetInnerResponse;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetResponse;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetTitleListResponse;
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

    public List<RecruitGetResponse> getTotalRecruitList(User user) {
        List<Recruit> recruits = recruitRepository.findAllByUserId(user.getId());

        return recruits.stream()
            .sorted((r1, r2) -> r2.getCreatedDate().compareTo(r1.getCreatedDate()))
            .map(RecruitGetResponse::from)
            .toList();
    }

    public List<RecruitGetResponse> getRecruitListBySeason(User user, String seasonName) {
        Season season = seasonService.getSeasonByName(user, seasonName);
        return recruitRepository.findAllByUserIdAndSeason(user.getId(), season).stream()
            .map(RecruitGetResponse::from)
            .toList();
    }

    public List<RecruitGetTitleListResponse> getRecruitRecent5TitleList(User user) {
        return recruitRepository.findTop5ByUserIdOrderByCreatedDateDesc(user.getId())
            .stream()
            .map(RecruitGetTitleListResponse::from)
            .toList();
    }

    // TODO: 진행중인 공고는 일정등록이 안된 것을 우선으로 그 이후에는 RecruitSchedule이 현재와 가까운 순으로 정렬한다.
    public List<RecruitGetResponse> getProgressingRecruitList(User user) {

        List<Recruit> recruits = recruitRepository.findAllByUserId(user.getId());

        // 불합격이 아닌 공고중에서 스케줄이 비어 있거나 지난 일정만 등록되어 있는 공고 중에서 지원 상태가 불합격인 공고를 분리해냄
        Map<Boolean, List<Recruit>> partitionedRecruitsByNeedingSchedule = partitionRecruits(recruits);

        List<Recruit> recruitsNeedingSchedule = partitionedRecruitsByNeedingSchedule.get(true);
        List<Recruit> recruitsWithSchedule = partitionedRecruitsByNeedingSchedule.get(false);

        //recruitsWithSchedule에서 시간이 지나지 않은 스케줄 중에서 가장 현재와 가까운 걸 기준으로 정렬
        List<Recruit> sortedRecruitsWithSchedule = recruitsWithSchedule.stream()
            .sorted(Comparator.comparing(this::getNearestUpcomingDate))
            .toList();

        recruitsNeedingSchedule.addAll(sortedRecruitsWithSchedule);

        return recruitsNeedingSchedule.stream()
            .map(RecruitGetResponse::from)
            .toList();
    }

    private LocalDate getNearestUpcomingDate(Recruit recruit) {
        return recruit.getScheduleList().stream()
            .map(RecruitSchedule::getDeadLine)
            .filter(deadLine -> deadLine.isAfter(LocalDate.now()))
            .min(Comparator.naturalOrder())
            .orElse(LocalDate.MAX);
    }

    private Map<Boolean, List<Recruit>> partitionRecruits(List<Recruit> recruits) {
        return recruits.stream()
            .filter(recruit -> !RecruitStatusCategory.isRejectionStatusOrFinalAcceptance(
                recruit.getRecruitStatus())) // 불합격 상태 필터링
            .collect(Collectors.partitioningBy(this::isNeedsScheduleUpdate));
    }

    private boolean isNeedsScheduleUpdate(Recruit recruit) {
        List<RecruitSchedule> scheduleList = recruit.getScheduleList();
        return scheduleList.isEmpty() || scheduleList.stream()
            .allMatch(schedule -> schedule.getDeadLine().isBefore(LocalDate.now()));
    }

    public RecruitGetInnerResponse getRecruit(User user, Long recruitId) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);
        return RecruitGetInnerResponse.from(recruit);
    }

    @Transactional
    public RecruitGetResponse createRecruit(User user, RecruitCreateRequest request) {

        Season season = seasonService.getSeasonByName(user, request.season());
        Recruit recruit = request.toEntity(user, season);
        // 공고 저장하여 id 확보
        Recruit savedRecruit = recruitRepository.save(recruit);

        addRecruitScheduleIfRequired(request, savedRecruit);

        // 공고에 공고 일정을 설정
        recruitRepository.save(recruit);

        return RecruitGetResponse.from(savedRecruit);
    }

    private void addRecruitScheduleIfRequired(RecruitCreateRequest request, Recruit recruit) {
        if (request.deadLine() != null && !request.deadLine().isEmpty()) {
            RecruitSchedule recruitSchedule = recruitScheduleService.createRecruitSchedule(
                recruit.getId(),
                RecruitScheduleCreateRequest.of(
                    request.recruitScheduleStage(), request.deadLine())
            );
            recruit.addSchedule(recruitSchedule);
        }
    }

    @Transactional
    public void deleteRecruit(User user, Long recruitId) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);

        recruitRepository.deleteById(recruit.getId());
    }

    @Transactional
    public RecruitGetResponse updateRecruitTitle(User user, Long recruitId, String title) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);

        recruit.updateTitle(title);

        return RecruitGetResponse.from(recruitRepository.save(recruit));
    }

    @Transactional
    public RecruitGetResponse updateRecruitSeason(User user, Long recruitId, String seasonName) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);
        Season season = seasonService.getSeasonByName(user, seasonName);
        recruit.updateSeason(season);

        return RecruitGetResponse.from(recruitRepository.save(recruit));
    }

    @Transactional
    public RecruitGetResponse updateRecruitStatus(User user, Long recruitId, String recruitStatus) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);

        recruit.updateRecruitStatus(RecruitStatus.findByValue(recruitStatus));

        return RecruitGetResponse.from(recruitRepository.save(recruit));
    }

    @Transactional
    public RecruitGetResponse updateRecruitSiteUrl(User user, Long recruitId, String siteUrl) {
        Recruit recruit = findRecruitByUserAndId(user, recruitId);

        recruit.updateSiteUrl(siteUrl);

        return RecruitGetResponse.from(recruitRepository.save(recruit));
    }

    private Recruit findRecruitByUserAndId(User user, Long recruitId) {
        return recruitRepository.findByUserIdAndId(user.getId(), recruitId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.RECRUIT_NOT_FOUND));
    }
}
