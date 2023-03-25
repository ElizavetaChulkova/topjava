package ru.javawebinar.topjava.web;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Converters {

    public static class DateConverter implements Converter<String, LocalDate> {

        @Override
        public LocalDate convert(String datePattern) {
            return LocalDate.parse(datePattern, DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    public static class TimeConverter implements Converter<String, LocalTime> {

        @Override
        public LocalTime convert(String datePattern) {
            return LocalTime.parse(datePattern, DateTimeFormatter.ISO_LOCAL_TIME);
        }
    }
}
