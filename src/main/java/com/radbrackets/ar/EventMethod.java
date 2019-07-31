package com.radbrackets.ar;

import java.lang.reflect.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class EventMethod {

    private final Class<?> eventType;
    private final Object target;
    private final Method method;

    EventMethod(Method method, Object target) {
        this.method = method;
        this.eventType = method.getParameterTypes()[0];
        this.target = target;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public void invoke(Object event) {
        try {
            method.setAccessible(true);
            method.invoke(target, event);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            throw new CantInvokeMethodException(ex);
        }
    }

    public static Predicate<Method> hasSingleParameter() {
        return method -> method.getParameterCount() == 1;
    }

    public static Predicate<Method> hasEventParameter() {
        return method -> Event.class.isAssignableFrom(method.getParameterTypes()[0]);
    }

    public static Predicate<Method> hasAnnotationEventHandler() {
        return method -> Stream.of(method.getDeclaredAnnotations()).anyMatch(annotation -> Apply.class.isAssignableFrom(annotation.getClass()));
    }

    public static Predicate<? super Method> isOnMethodName() {
        return method ->  "on".equals(method.getName());
    }

    public static class CantInvokeMethodException extends RuntimeException {
        public CantInvokeMethodException(Exception ex) {
            super(ex);
        }
    }

}
