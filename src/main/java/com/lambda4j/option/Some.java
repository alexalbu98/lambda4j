package com.lambda4j.option;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

class Some<A> extends Option<A> {
    private final A value;

    Some(A a) {
        value = a;
    }

    public A getOrThrow() {
        return value;
    }

    public A getOrElse(Supplier<? extends A> defaultValue) {
        return value;
    }

    public <B> Option<B> map(Function<? super A, ? extends B> f) {
        return new Some<>(f.apply(value));
    }

    public <B> Option<B> flatMap(Function<? super A, Option<B>> f) {
        return f.apply(value);
    }

    public String toString() {
        return String.format("Some(%s)", value);
    }
}
