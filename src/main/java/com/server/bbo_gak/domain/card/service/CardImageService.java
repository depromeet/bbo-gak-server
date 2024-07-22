package com.server.bbo_gak.domain.card.service;

import com.server.bbo_gak.domain.card.dto.request.CardImageDeleteRequest;
import com.server.bbo_gak.domain.card.dto.request.CardImageUploadCompleteRequest;
import com.server.bbo_gak.domain.card.dto.response.CardImageUploadCompleteResponse;
import java.util.List;

public interface CardImageService {

    List<CardImageUploadCompleteResponse> addImagesToCard(CardImageUploadCompleteRequest requests);

    void deleteCardImage(CardImageDeleteRequest request);

}
