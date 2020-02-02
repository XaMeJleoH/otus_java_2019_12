package ru.otus.hw.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class IoC {

    public static ActionInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new ActionImpl());
        return (ActionInterface) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{ActionInterface.class}, handler);
    }

    public static class DemoInvocationHandler implements InvocationHandler {
        private final ActionInterface myClass;

        DemoInvocationHandler(ActionInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("invoking method:" + method);
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }

}
