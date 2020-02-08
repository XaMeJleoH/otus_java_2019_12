package ru.otus.hw.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IoC {

    public static ActionInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new ActionImpl());
        return (ActionInterface) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{ActionInterface.class}, handler);
    }

    public static class DemoInvocationHandler implements InvocationHandler {
        private final ActionInterface myClass;
        private List<Method> myMethod;

        DemoInvocationHandler(ActionInterface myClass) {
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
            myMethod = new ArrayList<>();
            Arrays.stream(myClass.getClass().getDeclaredMethods()).forEach(method -> {
                if (method.getAnnotation(Log.class) != null) {
                    myMethod.add(method);
                }
            });
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
