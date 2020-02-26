package ru.otus.hw.custom.framework;

public class CustomFrameworkException extends Exception {
    public CustomFrameworkException(String errorMessage) {
        super(errorMessage);
    }

    public CustomFrameworkException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
