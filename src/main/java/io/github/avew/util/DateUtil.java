package io.github.avew.util;

import com.sun.istack.Nullable;
import io.github.avew.dto.StartEndDateResDTO;

import java.time.Instant;
import java.time.LocalDate;
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

    public static StartEndDateResDTO toInstant(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateOf = LocalDate.parse(startDate, formatter);
        LocalDate endDateOf = LocalDate.parse(endDate, formatter);
        Instant start = startDateOf.atStartOfDay().toInstant(ZoneOffset.of("+07:00"));
        Instant end = endDateOf.atStartOfDay().plusDays(1).toInstant(ZoneOffset.of("+07:00"));
        return StartEndDateResDTO.builder().start(start).end(end).build();
    }
}
