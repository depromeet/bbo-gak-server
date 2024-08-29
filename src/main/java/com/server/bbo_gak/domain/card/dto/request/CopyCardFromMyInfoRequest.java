package com.server.bbo_gak.domain.card.dto.request;

import jakarta.validation.constraints.Size;
import java.util.List;

public record CopyCardFromMyInfoRequest(

    @Size(min = 1, max = 3)
    List<String> cardTypeValueList,

    String cardTypeValueGroup
) {

    public CardTypeUpdateRequest toCardTypeUpdateRequest() {
        return new CardTypeUpdateRequest(cardTypeValueList, cardTypeValueGroup);
    }

}
