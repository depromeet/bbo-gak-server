package com.server.bbo_gak.domain.notification.entity;

import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String title;

    private String message;

    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Notification(String title, String message, boolean isRead, NotificationType type, Long referenceId,
        User user) {
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.type = type;
        this.referenceId = referenceId;
        this.user = user;
    }

    public static Notification fromRecruitAndSchedule(Recruit recruit, RecruitSchedule recruitSchedule) {
        return Notification.builder()
            .isRead(false)
            .user(recruit.getUser())
            .type(NotificationType.RECRUIT)
            .referenceId(recruit.getId())
            .title(recruit.getTitle())
            .message(createMessageByRecruitSchedule(recruitSchedule))
            .build();
    }

    public static String createMessageByRecruitSchedule(RecruitSchedule recruitSchedule) {
        return "공고의" + recruitSchedule.getRecruitScheduleStage().getValue() + "이 내일이예요!";
    }

    public void updateReadTrue() {
        isRead = true;
    }
}
