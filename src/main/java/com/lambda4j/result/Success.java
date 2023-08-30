package com.lambda4j.result;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

class Success<V> extends Result<V> {
    private final V value;

    Success(V value) {
        super();
        this.value = value;
    }

    public String toString() {
        return String.format("Success(%s)", value.toString());
    }

    public V getOrElse(V defaultValue) {
        return value;
    }

    public V getOrElse(Supplier<V> defaultValue) {
        return value;
    }

    public <U> Result<U> map(Function<V, U> f) {
        try {
            return success(f.apply(value));
        } catch (Exception e) {
            return failure(e);
        }
    }

    public <U> Result<U> flatMap(Function<V, Result<U>> f) {
        try {
            return f.apply(value);
        } catch (Exception e) {
            return failure(e);
        }
    }
}
