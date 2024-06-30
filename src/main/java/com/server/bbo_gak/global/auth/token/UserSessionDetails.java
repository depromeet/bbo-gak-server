package com.server.bbo_gak.global.auth.token;

import java.io.Serializable;
import lombok.Builder;

public class UserSessionDetails implements Serializable {

    private String email;

    @Builder
    public UserSessionDetails(String email) {
        this.email = email;
    }


}
