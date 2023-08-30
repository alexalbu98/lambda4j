package com.lambda4j.result.either;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

public abstract class Either<E, A> {
    public abstract <B> Either<E, B> map(Function<? super A, ? extends B> f);

    public abstract <B> Either<E, B> flatMap(Function<? super A, Either<E, B>> f);

    public abstract A getOrElse(Supplier<? extends A> defaultValue);

    public Either<E, A> orElse(Supplier<Either<E, A>> defaultValue) {
        return map(x -> this).getOrElse(defaultValue);
    }

    public static <E, A> Either<E, A> left(E value) {
        return new Left<>(value);
    }

    public static <E, A> Either<E, A> right(A value) {
        return new Right<>(value);
    }
}