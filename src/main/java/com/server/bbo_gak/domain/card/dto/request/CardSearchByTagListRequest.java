package com.server.bbo_gak.domain.card.dto.request;

import com.server.bbo_gak.domain.card.entity.CardTypeValue;
import com.server.bbo_gak.domain.card.entity.CardTypeValueGroup;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

public record CardSearchByTagListRequest(

    @Size(min = 1)
    List<Long> tagIdList,

    @Nullable
    String cardTypeValueGroup
) {

    public CardTypeValue[] getCardTypeValueList() {
        return Optional.ofNullable(cardTypeValueGroup)
            .map(cardTypeValueGroup -> CardTypeValueGroup.findByValue(cardTypeValueGroup).getCardTypeValueList())
            .orElseGet(() -> new CardTypeValue[0]);
    }
}
