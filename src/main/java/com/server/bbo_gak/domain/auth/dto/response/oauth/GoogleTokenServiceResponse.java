package com.server.bbo_gak.domain.auth.dto.response.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenServiceResponse(@JsonProperty("access_token") String accessToken) {

}
