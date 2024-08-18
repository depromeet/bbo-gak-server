package com.server.bbo_gak.domain.recruit.entity;

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

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitSchedule {

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

    public void setRecruit(Recruit recruit) {
        this.recruit = recruit;
    }

    public static RecruitSchedule of(Recruit recruit, RecruitScheduleStage recruitScheduleStage, String deadLine){
        return RecruitSchedule.builder()
                .recruit(recruit)
                .recruitScheduleStage(recruitScheduleStage)
                .deadLine(LocalDate.parse(deadLine)).build();
    }

}
