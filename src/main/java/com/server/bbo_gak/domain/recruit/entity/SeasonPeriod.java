package com.server.bbo_gak.domain.recruit.entity;

public enum SeasonPeriod {
    FIRST_HALF("상반기", 1, 6),
    SECOND_HALF("하반기", 7, 12);

    private final String name;
    private final int startMonth;
    private final int endMonth;

    SeasonPeriod(String name, int startMonth, int endMonth) {
        this.name = name;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
    }

    public static SeasonPeriod fromMonth(int month) {
        return month >= FIRST_HALF.startMonth && month <= FIRST_HALF.endMonth
            ? FIRST_HALF
            : SECOND_HALF;
    }

    public String getSeasonName(int year) {
        return year + "_" + this.name;
    }
}
