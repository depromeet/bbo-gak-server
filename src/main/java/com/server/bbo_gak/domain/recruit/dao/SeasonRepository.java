package com.server.bbo_gak.domain.recruit.dao;

import com.server.bbo_gak.domain.recruit.entity.Season;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    List<Season> findAllByUserId(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);

    Optional<Season> findByUserIdAndName(Long userId, String name);
}
