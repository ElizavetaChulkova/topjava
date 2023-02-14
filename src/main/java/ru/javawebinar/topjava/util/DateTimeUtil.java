package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T lt, T start, T end) {
        return lt.compareTo(start) >= 0 && lt.compareTo(end) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseDate(String date) {
        return date.isEmpty() ? LocalDate.MIN : LocalDate.parse(date);
    }

    public static LocalTime parseTime(String time) {
        return time.isEmpty() ? LocalTime.MIN : LocalTime.parse(time);
    }

    public static LocalTime parseTimeToMax(LocalTime time) {
        return time == LocalTime.MIN ? LocalTime.MAX : time;
    }

    public static LocalDateTime parseDateToMin(LocalDate date) {
        return date == LocalDate.MIN ? LocalDateTime.of(LocalDate.MIN, LocalTime.MIN) : date.atStartOfDay();
    }

    public static LocalDateTime parseDateToMax(LocalDate date) {
        return date == LocalDate.MIN ? LocalDateTime.of(LocalDate.MAX, LocalTime.MAX) :
                date.plusDays(1).atStartOfDay();
    }
}

