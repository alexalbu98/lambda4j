package com.lambda4j.tuple;

public class Tuple<T, U> {
    public final T first;
    public final U second;

    public Tuple(T t, U u) {
        this.first = t;
        this.second = u;
    }
}