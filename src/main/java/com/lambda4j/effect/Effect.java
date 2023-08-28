package com.lambda4j.effect;

public interface Effect<T> {
    void apply(T t);
}
