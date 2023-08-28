package com.lambda4j.function;

public interface Function<T, U> {
    U apply(T arg);

    default <V> Function<V, U> compose(Function<? super V, ? extends T> f) {
        return x -> apply(f.apply(x));
    }

    static <T, U, V> Function<V, U> compose(Function<? super T, ? extends U> f, Function<? super V, ? extends T> g) {
        return x -> f.apply(g.apply(x));
    }

    static <T, U, V> Function<Function<T, U>, Function<Function<U, V>, Function<T, V>>> compose() {
        return x -> y -> y.compose(x);
    }

    static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> higherCompose() {
        return (Function<U, V> x) -> (Function<T, U> y) -> (T z) -> x.apply(y.apply(z));
    }


    default <V> Function<T, V> andThen(Function<? super U, ? extends V> f) {
        return x -> f.apply(apply(x));
    }

    static <T, U, V> Function<T, V> andThen(Function<? super T, ? extends U> f, Function<? super U, ? extends V> g) {
        return x -> g.apply(f.apply(x));
    }

    static <T, U, V> Function<Function<T, U>, Function<Function<V, T>, Function<V, U>>> andThen() {
        return x -> y -> y.andThen(x);
    }

    static <T, U, V> Function<Function<T, U>, Function<Function<U, V>, Function<T, V>>> higherAndThen() {
        return x -> y -> z -> y.apply(x.apply(z));
    }

    static <T> Function<T, T> identity() {
        return t -> t;
    }

}