package com.lambda4j.result;


import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

import java.io.Serializable;

public abstract class Result<V> implements Serializable {
    Result() {
    }

    public abstract V getOrElse(final V defaultValue);

    public abstract V getOrElse(final Supplier<V> defaultValue);

    public abstract <U> Result<U> map(Function<V, U> f);

    public abstract <U> Result<U> flatMap(Function<V, Result<U>> f);

    public Result<V> orElse(Supplier<Result<V>> defaultValue) {
        return map(x -> this).getOrElse(defaultValue);
    }

    public static <V> Result<V> failure(String message) {
        return new Failure<>(message);
    }

    public static <V> Result<V> failure(Exception e) {
        return new Failure<>(e);
    }

    public static <V> Result<V> failure(RuntimeException e) {
        return new Failure<>(e);
    }

    public static <V> Result<V> success(V value) {
        return new Success<>(value);
    }
}