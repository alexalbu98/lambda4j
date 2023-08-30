package com.lambda4j.list;

import com.lambda4j.function.Function;
import com.lambda4j.recursion.TailCall;

import static com.lambda4j.recursion.TailCall.ret;
import static com.lambda4j.recursion.TailCall.sus;

public abstract class List<A> {
    List() {
    }

    public abstract A head();

    public abstract List<A> tail();

    public abstract boolean isEmpty();

    public abstract List<A> setHead(A h);

    public abstract List<A> drop(int n);

    public abstract List<A> reverse();

    public abstract List<A> dropWhile(Function<? super A, Boolean> f);

    public abstract List<A> removeLast();

    public abstract <B> B foldLeft(B identity, Function<? super B, Function<? super A, ? extends B>> f);

    public abstract <B> B foldRight(B identity, Function<? super A, Function<? super B, ? extends B>> f);

    public abstract <B> List<B> map(Function<? super A, ? extends B> f);

    public abstract List<A> filter(Function<? super A, Boolean> f);

    public abstract <B> List<B> flatMap(Function<? super A, List<B>> f);

    public List<A> append(A a) {
        return new Cons<>(a, this);
    }

    public int length() {
        return this.foldRight(0, x -> y -> y + 1);
    }

    public static <A> List<A> list() {
        return new Nil<>();
    }

    @SafeVarargs
    public static <A> List<A> list(A... a) {
        List<A> n = list();
        for (int i = a.length - 1; i >= 0; i--) {
            n = new Cons<>(a[i], n);
        }
        return n;
    }

    public static <A> List<A> list(java.util.List<? extends A> a) {
        List<A> n = list();
        for (int i = a.size() - 1; i >= 0; i--) {
            n = new Cons<>(a.get(i), n);
        }
        return n;
    }

    public static <A> List<A> concat(List<? extends A> list1, List<A> list2) {
        return concat_(list1.reverse(), list2).eval();
    }

    public static <A> List<A> flatten(List<List<A>> list) {
        return foldRight(list, List.list(), x -> y -> concat(x, y));
    }

    public static <A, B> B foldRight(List<? extends A> list, B identity, Function<? super A, Function<? super B, ? extends B>> f) {
        return foldRight_(identity, list.reverse(), f).eval();
    }

    private static <A> TailCall<List<A>> concat_(List<? extends A> list1, List<A> list2) {
        return list1.isEmpty()
                ? ret(list2)
                : sus(() -> concat_(list1.tail(), new Cons<>(list1.head(), list2)));
    }

    protected static <A, B> TailCall<B> foldRight_(B acc, List<? extends A> ts, Function<? super A, Function<? super B, ? extends B>> f) {
        return ts.isEmpty()
                ? ret(acc)
                : sus(() -> foldRight_(f.apply(ts.head()).apply(acc), ts.tail(), f));
    }

}