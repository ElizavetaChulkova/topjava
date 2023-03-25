package ru.javawebinar.topjava.web;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

public class DateTimeFormatter {
    public static class LocalDateFormatter implements Formatter<LocalDate> {

        @Override
        public LocalDate parse(String date, Locale locale) {
            return parseLocalDate(date);
        }

        @Override
        public String print(LocalDate date, Locale locale) {
            return date.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    public static class LocalTimeFormatter implements Formatter<LocalTime> {

        @Override
        public LocalTime parse(String time, Locale locale) {
            return parseLocalTime(time);
        }

        @Override
        public String print(LocalTime time, Locale locale) {
            return time.format(java.time.format.DateTimeFormatter.ISO_LOCAL_TIME);
        }
    }
}
