package ru.otus.hw.custom.framework;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.Set;

@Data
class CustomFrameworkMethods {
    private Set<Method> beforeMethods;
    private Set<Method> afterMethods;
    private Set<Method> testMethods;
}
