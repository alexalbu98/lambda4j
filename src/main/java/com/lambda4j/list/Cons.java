package com.lambda4j.list;

import com.lambda4j.function.Function;
import com.lambda4j.recursion.TailCall;

import static com.lambda4j.recursion.TailCall.ret;
import static com.lambda4j.recursion.TailCall.sus;

class Cons<A> extends List<A> {
    private final A head;
    private final List<A> tail;
    private final int length;

    Cons(A head, List<A> tail) {
        super();
        this.head = head;
        this.tail = tail;
        this.length = tail.lengthMemoized() + 1;
    }

    public A head() {
        return head;
    }

    public List<A> tail() {
        return tail;
    }

    public int lengthMemoized() {
        return length;
    }

    public boolean isEmpty() {
        return false;
    }

    public List<A> setHead(A h) {
        return new Cons<>(h, tail());
    }

    public List<A> drop(int n) {
        return n <= 0
                ? this
                : drop_(this, n).eval();
    }

    public List<A> reverse() {
        return reverse_(list(), this).eval();
    }

    public List<A> dropWhile(Function<? super A, Boolean> f) {
        return dropWhile_(this, f).eval();
    }

    public List<A> removeLast() {
        return reverse().tail().reverse();
    }

    public <B> B foldLeft(B identity, Function<? super B, Function<? super A, ? extends B>> f) {
        return foldLeft_(identity, this, f).eval();
    }

    public <B> B foldRight(B identity, Function<? super A, Function<? super B, ? extends B>> f) {
        return foldRight_(identity, this.reverse(), f).eval();
    }

    public <B> List<B> map(Function<? super A, ? extends B> f) {
        return foldRight(list(), h -> t -> new Cons<>(f.apply(h), t));
    }

    public List<A> filter(Function<? super A, Boolean> f) {
        return foldRight(list(), h -> t -> f.apply(h) ? new Cons<>(h, t) : t);
    }

    public <B> List<B> flatMap(Function<? super A, List<B>> f) {
        return foldRight(list(), h -> t -> concat(f.apply(h), t));
    }

    public String toString() {
        return String.format("[%sNIL]", toString_(new StringBuilder(), this).eval());
    }

    private TailCall<List<A>> reverse_(List<A> acc, List<? extends A> list) {
        return list.isEmpty()
                ? ret(acc)
                : sus(() -> reverse_(new Cons<>(list.head(), acc), list.tail()));
    }

    private TailCall<StringBuilder> toString_(StringBuilder acc, List<? extends A> list) {
        return list.isEmpty()
                ? ret(acc)
                : sus(() -> toString_(acc.append(list.head()).append(", "), list.tail()));
    }

    private TailCall<List<A>> drop_(List<A> list, int n) {
        return n <= 0 || list.isEmpty()
                ? ret(list)
                : sus(() -> drop_(list.tail(), n - 1));
    }

    private TailCall<List<A>> dropWhile_(List<A> list, Function<? super A, Boolean> f) {
        return !list.isEmpty() && f.apply(list.head())
                ? sus(() -> dropWhile_(list.tail(), f))
                : ret(list);
    }

    private <B> TailCall<B> foldLeft_(B acc, List<? extends A> list, Function<? super B, Function<? super A, ? extends B>> f) {
        return list.isEmpty()
                ? ret(acc)
                : sus(() -> foldLeft_(f.apply(acc).apply(list.head()), list.tail(), f));
    }
}
