package com.server.bbo_gak.domain.recruit.dao;

import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.recruit.entity.Season;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

    Optional<Recruit> findByUserIdAndId(Long userId, Long id);

    List<Recruit> findAllByUserId(Long userId);

    List<Recruit> findAllByUserIdAndSeason(Long userId, Season season);

    List<Recruit> findTop5ByUserIdOrderByCreatedDateAsc(Long userId);
}
