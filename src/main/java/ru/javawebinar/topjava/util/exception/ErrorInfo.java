package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;

    private final String message;

    private final String detail;

    public ErrorInfo(CharSequence url, ErrorType type, String message, String detail) {
        this.url = url.toString();
        this.type = type;
        this.message = message;
        this.detail = detail;
    }
}