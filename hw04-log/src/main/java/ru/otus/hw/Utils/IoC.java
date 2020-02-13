package ru.otus.hw.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class IoC {

    public static ActionInterface createMyClass() {
        InvocationHandler handler = new customInvocationHandler(new ActionImpl());
        return (ActionInterface) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{ActionInterface.class}, handler);
    }

    public static class customInvocationHandler implements InvocationHandler {
        private final ActionInterface myClass;
        private Set<Method> myMethod;

        customInvocationHandler(ActionInterface myClass) {
            this.myClass = myClass;
            runMyMethod();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (myMethod.contains(myClass.getClass().getMethod(method.getName(), method.getParameterTypes()))) {
                System.out.println("executed method: " + method.getName() + ", param: " + Arrays.toString(args));
            }
            return method.invoke(myClass, args);
        }

        private void runMyMethod() {
            myMethod = new HashSet<>();
            Arrays.stream(myClass.getClass().getDeclaredMethods())
                    .filter(method -> method.getAnnotation(Log.class) != null)
                    .forEach(myMethod :: add);
        }
    }
}
