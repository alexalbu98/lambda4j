package com.lambda4j.result;

import com.lambda4j.effect.Effect;
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

    public V getOrElse(Supplier<? extends V> defaultValue) {
        return value;
    }

    public <U> Result<U> map(Function<? super V, ? extends U> f) {
        try {
            return success(f.apply(value));
        } catch (Exception e) {
            return failure(e);
        }
    }

    public <U> Result<U> flatMap(Function<? super V, Result<U>> f) {
        try {
            return f.apply(value);
        } catch (Exception e) {
            return failure(e);
        }
    }

    public Result<V> mapFailure(String s) {
        return this;
    }

    public void forEach(Effect<V> ef) {
        ef.apply(value);
    }

    public void forEachOrThrow(Effect<V> ef) {
        ef.apply(value);
    }

    public Result<RuntimeException> forEachOrException(Effect<V> ef) {
        ef.apply(value);
        return empty();
    }
}
