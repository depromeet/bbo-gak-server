package com.server.bbo_gak.domain.card.dto.request;

import java.util.List;

public record CardTypeUpdateRequest(
    List<String> typeList
) {

}
