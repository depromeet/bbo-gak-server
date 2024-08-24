package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.Tag;
import com.server.bbo_gak.domain.user.entity.Job;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAllByJob(Job job);

    List<Tag> findAllByIdIsNotIn(List<Long> idList);

}
