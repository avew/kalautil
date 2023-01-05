package io.github.avew.util;

import com.sun.istack.Nullable;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static DateTimeFormatter dateTimeFormatter(@Nullable String pattern, @Nullable String offset) {
        if (pattern == null)
            pattern = "yyyy-MM-dd HH:mm:ss";
        if (offset == null)
            offset = "+07:00";
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneOffset.of(offset));
    }

    public static DateTimeFormatter instantFormatterAsiaJakarta() {
        return dateTimeFormatter("yyyy-MM-dd HH:mm:ss", "+07:00");
    }

    public static Instant instantFormatter(Instant now) {
        return Instant.from(instantFormatterAsiaJakarta().parse(now.toString()));
    }
}
