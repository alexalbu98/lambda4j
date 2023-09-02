package com.lambda4j.list.stream;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;
import com.lambda4j.list.List;
import com.lambda4j.recursion.TailCall;
import com.lambda4j.result.Result;

import static com.lambda4j.recursion.TailCall.ret;
import static com.lambda4j.recursion.TailCall.sus;

public abstract class Stream<A> {
    Stream() {
    }

    public abstract A head();

    public abstract Result<A> headOption();

    public abstract Stream<A> tail();

    public abstract Boolean isEmpty();

    public abstract Stream<A> take(int n);

    public abstract Stream<A> takeWhile(Function<A, Boolean> p);

    public abstract Stream<A> drop(int n);

    public abstract Stream<A> dropWhile(Function<A, Boolean> p);

    public abstract <B> B foldRight(Supplier<B> z, Function<A, Function<Supplier<B>, B>> f);

    public boolean exists(Function<A, Boolean> p) {
        return exists(this, p).eval();
    }

    public Stream<A> filter(Function<A, Boolean> p) {
        return foldRight(Stream::empty, a -> b -> p.apply(a)
                ? cons(() -> a, b)
                : b.get());
    }

    private TailCall<Boolean> exists(Stream<A> s, Function<A, Boolean> p) {
        return s.isEmpty()
                ? ret(false)
                : p.apply(s.head())
                ? ret(true)
                : sus(() -> exists(s.tail(), p));
    }

    public Stream<A> append(Supplier<Stream<A>> s) {
        return foldRight(s, a -> b -> cons(() -> a, b));
    }

    public <B> Stream<B> flatMap(Function<A, Stream<B>> f) {
        return foldRight(Stream::empty, a -> b -> f.apply(a).append(b));
    }

    public List<A> toList() {
        return toList(this, List.list()).eval().reverse();
    }

    private TailCall<List<A>> toList(Stream<A> s, List<A> acc) {
        return s.isEmpty()
                ? ret(acc)
                : sus(() -> toList(s.tail(), List.append(s.head(), acc)));
    }

    public static <A> Stream<A> empty() {
        return new Empty<>();
    }


    static <A> Stream<A> cons(Supplier<A> hd, Supplier<Stream<A>> tl) {
        return new Cons<>(hd, tl);
    }

    public static Stream<Integer> from(int i) {
        return cons(() -> i, () -> from(i + 1));
    }
}