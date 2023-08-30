package com.lambda4j.option;

import com.lambda4j.function.Function;

public abstract class Either<E, A> {

    public abstract <B> Either<E, B> map(Function<? super A, ? extends B> f);

    public abstract <B> Either<E, B> flatMap(Function<? super A, Either<E, B>> f);

    private static class Left<E, A> extends Either<E, A> {
        private final E value;

        private Left(E value) {
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
    }

    private static class Right<E, A> extends Either<E, A> {
        private final A value;

        private Right(A value) {
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
    }

    public static <E, A> Either<E, A> left(E value) {
        return new Left<>(value);
    }

    public static <E, A> Either<E, A> right(A value) {
        return new Right<>(value);
    }
}