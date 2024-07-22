package com.server.bbo_gak.domain.card.dto.request;

import java.util.List;

public record CardImageUploadCompleteRequest(
    Long cardId,
    List<String> fileNames
) {

}
