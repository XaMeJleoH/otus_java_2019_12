package ru.otus.hw.custom.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static ru.otus.hw.custom.framework.CustomFrameworkHelper.*;

public class CustomFramework {

    static final String BEFORE_EACH = "BEFORE_ANNOTATION";
    static final String TEST = "TEST";
    static final String AFTER_EACH = "AFTER_ANNOTATION";

    public static String run(String testClassName) throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, CustomFrameworkException {

        Class<?> testClass = Class.forName(testClassName);
        Method[] declaredMethods = testClass.getDeclaredMethods();
        Map<String, Set<Method>> methodsMap = fillMethodsMap(declaredMethods);
        Constructor<?> testClassConstructor = testClass.getConstructor();
        StringBuilder tempTestResult = new StringBuilder();
        Object instance;
        int passedTests = 0;

        for (Method method : methodsMap.get(TEST)) {
            instance = testClassConstructor.newInstance();
            try {
                callMethods(methodsMap.get(BEFORE_EACH), instance);
                passedTests = invokeCurrentTest(instance, method, tempTestResult) ? ++passedTests : passedTests;
            } catch (Exception ex) {
                throw new CustomFrameworkException(ex.getMessage());
            } finally {
                callMethods(methodsMap.get(AFTER_EACH), instance);
            }
        }

        StringBuilder testResult = new StringBuilder()
                .append("The test was finished for the class: ").append(testClassName).append("\r\n")
                .append("Count of tests= ").append(methodsMap.get(TEST).size()).append("\r\n")
                .append("Were passed ").append(passedTests).append(" tests").append("\r\n")
                .append("Were Not passed ").append(methodsMap.get(TEST).size()-passedTests).append(" tests").append("\r\n");

        return testResult.toString();
    }

    private static boolean invokeCurrentTest(Object instance, Method method, StringBuilder tempTestResult) {
        try {
            method.invoke(instance);
            tempTestResult.append(method.getName())
                    .append(" is passed.\r\n");
            return true;
        } catch (IndexOutOfBoundsException | IllegalArgumentException | IllegalAccessException ex) {
            tempTestResult.append(method.getName())
                    .append(" is NOT passed. Exception: ")
                    .append(ex.getMessage())
                    .append("\r\n");
        } catch (InvocationTargetException ex) {
            tempTestResult.append(method.getName())
                    .append(" is NOT passed. Exception: ")
                    .append(ex.getTargetException().getMessage())
                    .append("\r\n");
        }
        return false;
    }
}
