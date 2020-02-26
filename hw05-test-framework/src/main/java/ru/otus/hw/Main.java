package ru.otus.hw;

import ru.otus.hw.custom.framework.CustomFramework;
import ru.otus.hw.custom.framework.CustomFrameworkException;

public class Main {

    public static void main(String... args) throws CustomFrameworkException {
        System.out.println(CustomFramework.run("test.StringTest"));
    }
}
