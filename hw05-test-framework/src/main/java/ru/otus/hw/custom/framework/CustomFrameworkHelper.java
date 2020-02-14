package ru.otus.hw.custom.framework;

import ru.otus.hw.custom.framework.annotation.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static ru.otus.hw.custom.framework.CustomFramework.*;

class CustomFrameworkHelper {

    static Map<String, Set<Method>> fillMethodsMap(Method[] declaredMethods) {

        Map<String, Set<Method>> methods = new HashMap<>();
        methods.put(BEFORE_EACH, new HashSet<>());
        methods.put(TEST, new HashSet<>());
        methods.put(AFTER_EACH, new HashSet<>());

        for (Method method : declaredMethods) {
            if (method.getDeclaredAnnotation(BeforeAnnotation.class) != null) {
                methods.get(BEFORE_EACH).add(method);
            } else if (method.getDeclaredAnnotation(TestAnnotation.class) != null) {
                methods.get(TEST).add(method);
            } else if (method.getDeclaredAnnotation(AfterAnnotation.class) != null) {
                methods.get(AFTER_EACH).add(method);
            }
        }

        return methods;
    }


    static void callMethods(Set<Method> declaredMethods, Object instance) throws CustomFrameworkException {
        for (Method method : declaredMethods) {
            try {
                method.invoke(instance);
            } catch (Exception ex) {
                throw new CustomFrameworkException(ex.getMessage());
            }
        }
    }
}

