package com.server.bbo_gak.domain.card.dao;

import com.server.bbo_gak.domain.card.entity.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAllByIdIsNotIn(List<Long> idList);

}
