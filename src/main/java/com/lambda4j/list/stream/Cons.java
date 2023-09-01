package com.lambda4j.list.stream;

import com.lambda4j.function.Supplier;

public class Cons<A> extends Stream<A> {
    private final Supplier<A> head;
    private final Supplier<Stream<A>> tail;
    private A h;
    private Stream<A> tl;

    Cons(Supplier<A> h, Supplier<Stream<A>> t) {
        super();
        head = h;
        tail = t;
    }

    @Override
    public A head() {
        if (h == null) {
            h = head.get();
        }
        return h;
    }

    @Override
    public Stream<A> tail() {
        if (tl == null) {
            tl = tail.get();
        }
        return tl;
    }

    @Override
    public Boolean isEmpty() {
        return false;
    }
}
