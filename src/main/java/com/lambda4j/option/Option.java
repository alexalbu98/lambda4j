package com.lambda4j.option;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

public abstract class Option<A> {
    @SuppressWarnings("rawtypes")
    private static final Option none = new None();

    protected abstract A getOrThrow();

    public abstract A getOrElse(Supplier<? extends A> defaultValue);

    public abstract <B> Option<B> map(Function<? super A, ? extends B> f);

    public abstract <B> Option<B> flatMap(Function<? super A, Option<B>> f);

    public Option<A> orElse(Supplier<Option<A>> defaultValue) {
        return map(x -> this).getOrElse(defaultValue);
    }

    public Option<A> filter(Function<A, Boolean> f) {
        return flatMap(x -> f.apply(x)
                ? this
                : none());
    }

    private static class None<A> extends Option<A> {
        private None() {
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

    public static <A> Option<A> some(A a) {
        return new Some<>(a);
    }

    @SuppressWarnings("unchecked")
    public static <A> Option<A> none() {
        return none;
    }
}