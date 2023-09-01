package com.lambda4j.list.stream;

class Empty<A> extends Stream<A> {
    @Override
    public Stream<A> tail() {
        throw new IllegalStateException("tail called on empty");
    }

    @Override
    public A head() {
        throw new IllegalStateException("head called on empty");
    }

    @Override
    public Boolean isEmpty() {
        return true;
    }
}
