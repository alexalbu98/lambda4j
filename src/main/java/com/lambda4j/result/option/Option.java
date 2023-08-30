package com.lambda4j.result.option;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

public abstract class Option<A> {
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

    public static <A> Option<A> some(A a) {
        return new Some<>(a);
    }

    public static <A> Option<A> none() {
        return new None<>();
    }
}