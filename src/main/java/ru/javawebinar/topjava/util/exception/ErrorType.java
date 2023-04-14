package ru.javawebinar.topjava.util.exception;

public enum ErrorType {
    APP_ERROR("common.appError"),
    DATA_NOT_FOUND("common.dataNotFoundError"),
    DATA_ERROR("common.dataError"),
    VALIDATION_ERROR("common.validationError");

    private String errorMessage;

    ErrorType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
