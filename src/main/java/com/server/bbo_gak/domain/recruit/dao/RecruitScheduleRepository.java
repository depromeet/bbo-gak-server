package com.server.bbo_gak.domain.recruit.dao;

import com.server.bbo_gak.domain.recruit.entity.RecruitSchedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitScheduleRepository extends JpaRepository<RecruitSchedule, Long> {

    List<RecruitSchedule> findAllByDeadLineBetween(LocalDate now, LocalDate nowPlusOneDay);
}
