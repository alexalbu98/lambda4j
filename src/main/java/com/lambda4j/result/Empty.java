package com.lambda4j.result;

import com.lambda4j.effect.Effect;
import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

class Empty<V> extends Result<V> {
    public Empty() {
        super();
    }

    public boolean isSuccess() {
        return false;
    }

    public boolean isFailure() {
        return false;
    }

    public V getOrElse(final V defaultValue) {
        return defaultValue;
    }

    public <U> Result<U> map(Function<? super V, ? extends U> f) {
        return new Empty<>();
    }

    public <U> Result<U> flatMap(Function<? super V, Result<U>> f) {
        return new Empty<>();
    }

    public Result<V> mapFailure(String s) {
        return this;
    }

    public void forEach(Effect<V> ef) {

    }

    public void forEachOrThrow(Effect<V> ef) {

    }

    public Result<RuntimeException> forEachOrException(Effect<V> ef) {
        return empty();
    }

    public Result<String> forEachOrFail(Effect<V> c) {
        return empty();
    }

    public String toString() {
        return "Empty()";
    }

    public V getOrElse(Supplier<? extends V> defaultValue) {
        return defaultValue.get();
    }
}