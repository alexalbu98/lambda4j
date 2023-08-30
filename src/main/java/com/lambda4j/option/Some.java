package com.lambda4j.option;

import com.lambda4j.function.Supplier;

class Some<A> extends Option<A> {
    private final A value;

    Some(A a) {
        value = a;
    }

    public A getOrThrow() {
        return this.value;
    }

    public A getOrElse(Supplier<A> defaultValue) {
        return this.value;
    }

    public String toString() {
        return String.format("Some(%s)", this.value);
    }
}
