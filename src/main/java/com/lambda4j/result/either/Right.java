package com.lambda4j.result.either;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

class Right<E, A> extends Either<E, A> {
    private final A value;

    Right(A value) {
        this.value = value;
    }

    public String toString() {
        return String.format("Right(%s)", value);
    }

    public <B> Either<E, B> map(Function<? super A, ? extends B> f) {
        return new Right<>(f.apply(value));
    }

    public <B> Either<E, B> flatMap(Function<? super A, Either<E, B>> f) {
        return f.apply(value);
    }

    public A getOrElse(Supplier<? extends A> defaultValue) {
        return value;
    }
}

