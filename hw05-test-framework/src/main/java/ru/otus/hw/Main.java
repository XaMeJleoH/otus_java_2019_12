package ru.otus.hw;

import ru.otus.hw.custom.framework.CustomFramework;
import ru.otus.hw.custom.framework.CustomFrameworkException;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String... args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, CustomFrameworkException {
        System.out.println(CustomFramework.run("test.StringTest"));
    }
}
