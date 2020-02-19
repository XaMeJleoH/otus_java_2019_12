package ru.otus.hw.custom.framework;

import ru.otus.hw.custom.framework.annotation.*;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

class CustomFrameworkHelper {

    static CustomFrameworkMethods fillMethods(Method[] declaredMethods) {

        CustomFrameworkMethods customFrameworkMethods = new CustomFrameworkMethods();
        Set<Method> beforeSet = new HashSet<>();
        Set<Method> testSet = new HashSet<>();
        Set<Method> afterSet = new HashSet<>();

        for (Method method : declaredMethods) {
            if (method.getDeclaredAnnotation(Before.class) != null) {
                beforeSet.add(method);
            } else if (method.getDeclaredAnnotation(Test.class) != null) {
                testSet.add(method);
            } else if (method.getDeclaredAnnotation(After.class) != null) {
                afterSet.add(method);
            }
        }
        customFrameworkMethods.setBeforeMethods(beforeSet);
        customFrameworkMethods.setTestMethods(testSet);
        customFrameworkMethods.setAfterMethods(afterSet);

        return customFrameworkMethods;
    }


    static void callMethods(Set<Method> declaredMethods, Object instance) throws CustomFrameworkException {
        for (Method method : declaredMethods) {
            try {
                method.invoke(instance);
            } catch (Exception ex) {
                throw new CustomFrameworkException(ex.getMessage(), ex);
            }
        }
    }
}

