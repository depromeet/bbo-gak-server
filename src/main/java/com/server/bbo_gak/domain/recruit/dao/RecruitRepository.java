package com.server.bbo_gak.domain.recruit.dao;

import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.recruit.entity.Season;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

    Optional<Recruit> findByUserIdAndId(Long userId, Long id);

    List<Recruit> findAllByUserId(Long userId);

    List<Recruit> findAllByUserIdAndSeason(Long userId, Season season);

    @Query("SELECT r FROM Recruit r WHERE r.deadline > :currentDate AND r.user.id = :userId")
    List<Recruit> findAllByDeadlineAfterAndUserId(@Param("currentDate") LocalDate currentDate,
        @Param("userId") Long userId);
}
