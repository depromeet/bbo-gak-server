package com.server.bbo_gak.domain.recruit.entity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

    Optional<Recruit> findByUserId(Long userId);

    List<Recruit> findAllByUserId(Long userId);

}
