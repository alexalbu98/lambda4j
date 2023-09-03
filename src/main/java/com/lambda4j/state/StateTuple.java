package com.lambda4j.state;

public class StateTuple<A, S> {
    public final A value;
    public final S state;
    public StateTuple(A a, S s) {
        value = a;
        state = s;
    }
}