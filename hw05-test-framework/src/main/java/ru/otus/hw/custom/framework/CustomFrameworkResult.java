package ru.otus.hw.custom.framework;

import lombok.Data;

@Data
public class CustomFrameworkResult {
    private String className;
    private int countAllTest;
    private int countPassedTest;
    private int countNotPassedTest;


    @Override
    public String toString() {
        return "The test was finished for the class: " + className + "\r\n" +
                "Count of tests= " + countAllTest + "\r\n" +
                "Were passed " + countNotPassedTest + " tests" + "\r\n" +
                "Were Not passed " + countNotPassedTest + " tests" + "\r\n";
    }
}
