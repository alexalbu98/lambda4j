package com.lambda4j.memoization;

import com.lambda4j.function.Function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Memoizer<T, U> {
    private final Map<T, U> cache = new ConcurrentHashMap<>();

    private Memoizer() {
    }

    public static <T, U> Function<T, U> memoize(Function<? super T, ? extends U> function) {
        return new Memoizer<T, U>().doMemoize(function);
    }

    private Function<T, U> doMemoize(Function<? super T, ? extends U> function) {
        return input -> cache.computeIfAbsent(input, function::apply);
    }
}