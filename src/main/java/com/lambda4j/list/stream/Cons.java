package com.lambda4j.list.stream;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;
import com.lambda4j.list.List;
import com.lambda4j.recursion.TailCall;
import com.lambda4j.result.Result;

import static com.lambda4j.recursion.TailCall.ret;
import static com.lambda4j.recursion.TailCall.sus;

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
    public Result<A> headOption() {
        return Result.success(head());
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

    @Override
    public Stream<A> take(int n) {
        return n <= 0
                ? empty()
                : cons(head, () -> tail().take(n - 1));
    }

    @Override
    public Stream<A> takeWhile(Function<A, Boolean> p) {
        return p.apply(head())
                ? cons(head, () -> tail().takeWhile(p))
                : empty();
    }

    @Override
    public Stream<A> drop(int n) {
        return drop(this, n).eval();
    }

    private TailCall<Stream<A>> drop(Stream<A> acc, int n) {
        return n <= 0
                ? ret(acc)
                : sus(() -> drop(acc.tail(), n - 1));
    }

    @Override
    public Stream<A> dropWhile(Function<A, Boolean> p) {
        return dropWhile(this, p).eval();
    }

    private TailCall<Stream<A>> dropWhile(Stream<A> acc, Function<A, Boolean> p) {
        return acc.isEmpty()
                ? ret(acc)
                : p.apply(acc.head())
                ? sus(() -> dropWhile(acc.tail(), p)) : ret(acc);
    }

    @Override
    public <B> B foldRight(Supplier<B> z, Function<A, Function<Supplier<B>, B>> f) {
        return foldRight_(z, this, f).eval();
    }

    private static <A, B> TailCall<B> foldRight_(Supplier<B> acc, Stream<A> stream, Function<A, Function<Supplier<B>, B>> f) {
        return stream.isEmpty()
                ? ret(acc.get())
                : sus(() -> foldRight_(() -> f.apply(stream.head()).apply(acc), stream.tail(), f));
    }

    @Override
    public <B> B foldLeft(Supplier<B> z, Function<Supplier<B>, Function<A, B>> f) {
        return foldLeft_(z, this, f).eval();
    }

    private static <A, B> TailCall<B> foldLeft_(Supplier<B> acc, Stream<A> stream, Function<Supplier<B>, Function<A, B>> f) {
        return stream.isEmpty()
                ? ret(acc.get())
                : sus(() -> foldLeft_(() -> f.apply(acc).apply(stream.head()), stream.tail(), f));
    }
}
