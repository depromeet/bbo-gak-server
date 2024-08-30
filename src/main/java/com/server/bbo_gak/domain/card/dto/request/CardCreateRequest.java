package com.server.bbo_gak.domain.card.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CardCreateRequest(

    @Size(min = 1, max = 3)
    List<String> cardTypeValueList,

    @Size(max = 3)
    List<Long> tagIdList,

    @Nullable
    Long recruitId,

    String cardTypeValueGroup
) {

}
