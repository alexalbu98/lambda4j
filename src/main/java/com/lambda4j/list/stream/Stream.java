package com.lambda4j.list.stream;

import com.lambda4j.function.Supplier;

public abstract class Stream<A> {
    Stream() {
    }

    public abstract A head();

    public abstract Stream<A> tail();

    public abstract Boolean isEmpty();

    public static <A> Stream<A> empty() {
        return new Empty<>();
    }

    static <A> Stream<A> append(Supplier<A> hd, Supplier<Stream<A>> tl) {
        return new Cons<>(hd, tl);
    }

    public static Stream<Integer> from(int i) {
        return append(() -> i, () -> from(i + 1));
    }
}