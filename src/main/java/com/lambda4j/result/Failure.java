package com.lambda4j.result;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

class Failure<V> extends Result<V> {
    private final RuntimeException exception;

    Failure(String message) {
        super();
        this.exception = new IllegalStateException(message);
    }

    Failure(RuntimeException e) {
        super();
        this.exception = e;
    }

    Failure(Exception e) {
        super();
        this.exception = new IllegalStateException(e.getMessage(), e);
    }

    public String toString() {
        return String.format("Failure(%s)", exception.getMessage());
    }

    public V getOrElse(V defaultValue) {
        return defaultValue;
    }

    public V getOrElse(Supplier<V> defaultValue) {
        return defaultValue.get();
    }

    public <U> Result<U> map(Function<V, U> f) {
        return failure(exception);
    }

    public <U> Result<U> flatMap(Function<V, Result<U>> f) {
        return failure(exception);
    }
}
