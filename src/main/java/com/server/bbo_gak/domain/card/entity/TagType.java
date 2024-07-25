package com.server.bbo_gak.domain.card.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagType {

    CAPABILITY("역량"),
    PERSONALITY("인성");

    private final String value;
}
