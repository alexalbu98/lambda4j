package com.lambda4j.result.option;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

class None<A> extends Option<A> {

    None() {
    }

    public A getOrThrow() {
        throw new IllegalStateException("get called on None");
    }

    public A getOrElse(Supplier<? extends A> defaultValue) {
        return defaultValue.get();
    }

    public <B> Option<B> map(Function<? super A, ? extends B> f) {
        return none();
    }

    public <B> Option<B> flatMap(Function<? super A, Option<B>> f) {
        return none();
    }

    public String toString() {
        return "None";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof None;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
