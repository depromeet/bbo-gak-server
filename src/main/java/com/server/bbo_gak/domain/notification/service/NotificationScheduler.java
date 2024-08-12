package com.server.bbo_gak.domain.notification.service;

import com.server.bbo_gak.domain.notification.dao.NotificationRepository;
import com.server.bbo_gak.domain.notification.entity.Notification;
import com.server.bbo_gak.domain.recruit.dao.RecruitRepository;
import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
import com.server.bbo_gak.domain.recruit.entity.RecruitStatusCategory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final RecruitRepository recruitRepository;
    private final NotificationRepository notificationRepository;

    //TODO:스케줄러 추가시 application.yml의 ThreadPool 개수를 증가시켜줘야합니다.

    @Scheduled(cron = "0 0 0 * * ?")
    public void executeAtMidnight() {
        log.info("유저별 마감 하루 남은 공고 알림 생성");
        List<Recruit> allRecruits = recruitRepository.findAll();

        List<Entry<Recruit, RecruitSchedule>> recruitsWithOneDayLeft = findRecruitsWithOneDayLeft(allRecruits);
        List<Notification> notifications = createNotifications(recruitsWithOneDayLeft);

        notificationRepository.saveAll(notifications);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldNotificationsAtMidnight() {
        log.info("오래된 알림 삭제");

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        List<Notification> notificationsToDelete = notificationRepository.findAll().stream()
            .filter(notification -> notification.getCreatedDate().isBefore(thirtyDaysAgo))
            .toList();

        notificationRepository.deleteAll(notificationsToDelete);
    }

    private List<Entry<Recruit, RecruitSchedule>> findRecruitsWithOneDayLeft(List<Recruit> allRecruits) {
        return allRecruits.stream()
            .filter(recruit -> !RecruitStatusCategory.isRejectionStatus(recruit.getRecruitStatus()))
            .flatMap(recruit -> recruit.getScheduleList().stream()
                .filter(this::isDeadlineWithinOneDay)
                .map(schedule -> new SimpleEntry<>(recruit, schedule))
            )
            .collect(Collectors.toList());
    }

    private boolean isDeadlineWithinOneDay(RecruitSchedule schedule) {
        LocalDate now = LocalDate.now();
        LocalDate deadline = schedule.getDeadLine();
        return (deadline.equals(now) || deadline.equals(now.plusDays(1)));
    }

    private List<Notification> createNotifications(List<Entry<Recruit, RecruitSchedule>> recruitsWithOneDayLeft) {
        return recruitsWithOneDayLeft.stream()
            .map(entry -> Notification.fromRecruitAndSchedule(entry.getKey(), entry.getValue()))
            .toList();
    }
}