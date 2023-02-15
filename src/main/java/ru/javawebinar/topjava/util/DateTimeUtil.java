package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T local, T start, T end) {
        return local.compareTo(start) >= 0 && local.compareTo(end) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseDate(String date) {
        return date.isEmpty() ? null : LocalDate.parse(date);
    }

    public static LocalTime parseTime(String time) {
        return time.isEmpty() ? null : LocalTime.parse(time);
    }
}

