package ru.otus.hw;

import ru.otus.hw.Utils.ActionInterface;
import ru.otus.hw.Utils.IoC;

public class Action {

    public static void main(String... args) {
        ActionInterface actionInterface = IoC.createMyClass();
        actionInterface.calculation(6);
    }
}
