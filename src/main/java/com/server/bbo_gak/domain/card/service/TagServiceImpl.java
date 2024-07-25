package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dao.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public void getTagList() {

    }
}
