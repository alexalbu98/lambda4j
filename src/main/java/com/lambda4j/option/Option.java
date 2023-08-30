package com.lambda4j.option;

import com.lambda4j.function.Supplier;

public abstract class Option<A> {
    @SuppressWarnings("rawtypes")
    private static final Option none = new None();

    protected abstract A getOrThrow();

    public abstract A getOrElse(Supplier<A> defaultValue);

    private static class None<A> extends Option<A> {
        private None() {
        }

        public A getOrThrow() {
            throw new IllegalStateException("get called on None");
        }

        public A getOrElse(Supplier<A> defaultValue) {
            return defaultValue.get();
        }

        public String toString() {
            return "None";
        }
    }

    public static <A> Option<A> some(A a) {
        return new Some<>(a);
    }

    @SuppressWarnings("unchecked")
    public static <A> Option<A> none() {
        return none;
    }
}