package com.song.project.armeria.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Comparator;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Orderer implements Comparator<Object> {

    public static Orderer getInstance() {
        return LazyInstance.INSTANCE;
    }

    @Override
    public int compare(Object o1, Object o2) {
        return Integer.compare(getIntValue(o2), getIntValue(o1));
    }

    private int getIntValue( final Object obj) {
        return Optional.ofNullable(obj)
                .map(Object::getClass)
                .map(clazz -> clazz.getAnnotation(Order.class))
                .map(Order::value)
                .orElse(Ordered.LOWEST_PRECEDENCE);
    }

    private static class LazyInstance {
        private static final Orderer INSTANCE = new Orderer();
    }
}