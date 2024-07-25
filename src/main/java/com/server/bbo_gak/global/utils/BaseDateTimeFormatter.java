package com.server.bbo_gak.global.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BaseDateTimeFormatter {

    public static DateTimeFormatter getLocalDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withLocale(Locale.KOREA)
            .withZone(ZoneId.systemDefault());
    }
}
