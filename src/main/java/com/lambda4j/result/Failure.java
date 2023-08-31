package com.lambda4j.result;

import com.lambda4j.effect.Effect;
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

    public boolean isSuccess() {
        return false;
    }

    public boolean isFailure() {
        return true;
    }

    public V getOrElse(V defaultValue) {
        return defaultValue;
    }

    public V getOrElse(Supplier<? extends V> defaultValue) {
        return defaultValue.get();
    }

    public <U> Result<U> map(Function<? super V, ? extends U> f) {
        return failure(exception);
    }

    public <U> Result<U> flatMap(Function<? super V, Result<U>> f) {
        return failure(exception);
    }

    public Result<V> mapFailure(String s) {
        return failure(new IllegalStateException(s, exception));
    }

    public void forEach(Effect<V> ef) {

    }

    public void forEachOrThrow(Effect<V> ef) {
        throw exception;
    }

    public Result<RuntimeException> forEachOrException(Effect<V> ef) {
        return success(exception);
    }
}
