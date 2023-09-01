package com.lambda4j.list.stream;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;
import com.lambda4j.result.Result;

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
    public Result<A> headOption() {
        return Result.empty();
    }

    @Override
    public Boolean isEmpty() {
        return true;
    }

    @Override
    public Stream<A> take(int n) {
        return this;
    }

    @Override
    public Stream<A> takeWhile(Function<A, Boolean> p) {
        return this;
    }

    @Override
    public Stream<A> drop(int n) {
        return this;
    }

    @Override
    public Stream<A> dropWhile(Function<A, Boolean> p) {
        return this;
    }

    @Override
    public <B> B foldRight(Supplier<B> z, Function<A, Function<Supplier<B>, B>> f) {
        return z.get();
    }
}
