package com.server.bbo_gak.domain.notification.service;

import com.server.bbo_gak.domain.notification.dao.NotificationRepository;
import com.server.bbo_gak.domain.notification.entity.Notification;
import com.server.bbo_gak.domain.recruit.dao.RecruitScheduleRepository;
import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final RecruitScheduleRepository recruitScheduleRepository;

    //TODO:스케줄러 추가시 application.yml의 ThreadPool 개수를 증가시켜줘야합니다.

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void executeAtMidnight() {
        log.info("유저별 마감 하루 남은 공고 알림 생성");

        LocalDate now = LocalDate.now();
        List<RecruitSchedule> scheduleList = recruitScheduleRepository.findAllByDeadLineBetween(now,
            now.plusDays(1));

        List<Entry<Recruit, RecruitSchedule>> recruitsWithOneDayLeft = getRecruitAndScheduleMaps(scheduleList);

        List<Notification> notifications = createNotifications(recruitsWithOneDayLeft);

        notificationRepository.saveAll(notifications);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteOldNotificationsAtMidnight() {
        log.info("오래된 알림 삭제");

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        List<Notification> notificationsToDelete = notificationRepository.findAll().stream()
            .filter(notification -> notification.getCreatedDate().isBefore(thirtyDaysAgo))
            .toList();

        notificationRepository.deleteAll(notificationsToDelete);
    }

    private List<Entry<Recruit, RecruitSchedule>> getRecruitAndScheduleMaps(List<RecruitSchedule> scheduleList) {
        return scheduleList.stream()
            .map(schedule -> new SimpleEntry<>(schedule.getRecruit(), schedule))
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