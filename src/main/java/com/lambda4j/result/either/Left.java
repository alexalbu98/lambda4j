package com.lambda4j.result.either;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

class Left<E, A> extends Either<E, A> {
    private final E value;

    Left(E value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Left(%s)", value);
    }

    @Override
    public <B> Either<E, B> map(Function<? super A, ? extends B> f) {
        return new Left<>(value);
    }

    public <B> Either<E, B> flatMap(Function<? super A, Either<E, B>> f) {
        return new Left<>(value);
    }

    public A getOrElse(Supplier<? extends A> defaultValue) {
        return defaultValue.get();
    }
}