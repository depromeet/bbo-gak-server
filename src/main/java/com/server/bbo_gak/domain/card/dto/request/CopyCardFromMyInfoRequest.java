package com.server.bbo_gak.domain.card.dto.request;

import com.server.bbo_gak.domain.card.entity.CardTypeValueGroup;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CopyCardFromMyInfoRequest(

    @Size(min = 1, max = 3)
    List<String> cardTypeValueList
) {

    public CardTypeUpdateRequest toCardTypeUpdateRequest() {
        return new CardTypeUpdateRequest(cardTypeValueList, CardTypeValueGroup.RECRUIT.getValue());
    }
}
