package ru.otus.hw.custom.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static ru.otus.hw.custom.framework.CustomFrameworkHelper.*;

public class CustomFramework {


    public static CustomFrameworkResult run(String testClassName) throws CustomFrameworkException {
        try {
            Class<?> testClass = Class.forName(testClassName);
            Method[] declaredMethods = testClass.getDeclaredMethods();
            CustomFrameworkMethods methods = fillMethods(declaredMethods);
            Constructor<?> testClassConstructor = testClass.getConstructor();
            Object instance;
            int passedTests = 0;

            for (Method method : methods.getTestMethods()) {
                StringBuilder tempTestResult = new StringBuilder();
                instance = testClassConstructor.newInstance();
                try {
                    callMethods(methods.getBeforeMethods(), instance);
                    passedTests = invokeCurrentTest(instance, method, tempTestResult) ? ++passedTests : passedTests;
                    System.out.println(tempTestResult);
                } catch (Exception ex) {
                    throw new CustomFrameworkException(ex.getMessage());
                } finally {
                    callMethods(methods.getAfterMethods(), instance);
                }
            }

            CustomFrameworkResult customFrameworkResult = new CustomFrameworkResult();
            customFrameworkResult.setClassName(testClassName);
            customFrameworkResult.setCountAllTest(methods.getTestMethods().size());
            customFrameworkResult.setCountPassedTest(passedTests);
            customFrameworkResult.setCountNotPassedTest(methods.getTestMethods().size() - passedTests);

            return customFrameworkResult;
        } catch (Exception ex) {
            throw new CustomFrameworkException("Что-то пошло не так. Exception: " + ex.getMessage(), ex);
        }
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
                    .append(ex)
                    .append("\r\n");
        } catch (InvocationTargetException ex) {
            tempTestResult.append(method.getName())
                    .append(" is NOT passed. Exception: ")
                    .append(ex.getTargetException().getMessage())
                    .append(ex)
                    .append("\r\n");
        }
        return false;
    }
}
