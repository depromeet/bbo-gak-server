package com.server.bbo_gak.domain.recruit.entity;

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
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE recruit_schedule SET deleted = true WHERE recruit_schedule_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @Enumerated(EnumType.STRING)
    private RecruitScheduleStage recruitScheduleStage;

    private LocalDate deadLine;

    @Builder
    public RecruitSchedule(Recruit recruit, RecruitScheduleStage recruitScheduleStage, LocalDate deadLine) {
        this.recruit = recruit;
        this.recruitScheduleStage = recruitScheduleStage;
        this.deadLine = deadLine;
    }

    public static RecruitSchedule of(Recruit recruit, String recruitScheduleStage, String deadLine) {
        return RecruitSchedule.builder()
            .recruit(recruit)
            .recruitScheduleStage(RecruitScheduleStage.findByValue(recruitScheduleStage))
            .deadLine(LocalDate.parse(deadLine)).build();
    }

    public int getDDay() {
        return LocalDate.now().until(this.deadLine).getDays();
    }

    public void setRecruit(Recruit recruit) {
        this.recruit = recruit;
    }

    public void updateRecruitScheduleStage(String stage) {
        this.recruitScheduleStage = RecruitScheduleStage.findByValue(stage);
    }

    public void updateDeadLine(String deadLine) {
        this.deadLine = LocalDate.parse(deadLine);
    }

}
