package com.fu.basedemo.designpattern.singleton.usecase.async;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;

public class AsyncInvocationHandler implements InvocationHandler {

    private final Object target;
    private final ExecutorService executorService;

    public AsyncInvocationHandler(Object target) {
        this.target = target;
        this.executorService = AsyncExecutor.getInstance();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Async.class)) {
            executorService.submit(() -> {
                try {
                    method.invoke(target, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
            return null;
        }
        return method.invoke(target, args);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createAsyncProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new AsyncInvocationHandler(target)
        );
    }
}

