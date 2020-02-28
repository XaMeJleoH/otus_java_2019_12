package ru.otus.hw;

public class ATMException extends Exception {
    public ATMException(String errorMessage) {
        super(errorMessage);
    }

    public ATMException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
