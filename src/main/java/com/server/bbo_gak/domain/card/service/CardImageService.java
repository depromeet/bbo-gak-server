package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dto.request.CardImageDeleteRequest;

public interface CardImageService {

    void addImage();

    void deleteCardImage(CardImageDeleteRequest request);

}
