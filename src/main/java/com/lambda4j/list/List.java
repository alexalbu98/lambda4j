package com.lambda4j.list;

import com.lambda4j.function.Function;
import com.lambda4j.recursion.TailCall;

import static com.lambda4j.recursion.TailCall.ret;
import static com.lambda4j.recursion.TailCall.sus;

public abstract class List<A> {
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

    public List<A> append(A a) {
        return new Cons<>(a, this);
    }

    public int length() {
        return foldRight(0, x -> y -> y + 1);
    }

    @SuppressWarnings("unchecked")
    public static <A> List<A> list() {
        return NIL;
    }

    @SafeVarargs
    public static <A> List<A> list(A... a) {
        List<A> n = list();
        for (int i = a.length - 1; i >= 0; i--) {
            n = new Cons<>(a[i], n);
        }
        return n;
    }

    public static <A> List<A> concat(List<? extends A> list1, List<A> list2) {
        return concat(list(), list1, list2).eval();
    }

    private static <A> TailCall<List<A>> concat(List<A> acc, List<? extends A> list1, List<A> list2) {
        return list1.isEmpty()
                ? ret(acc)
                : sus(() -> concat(new Cons<>(list1.head(), list2), list1.tail(), list2));
    }

    @SuppressWarnings("rawtypes")
    public static final List NIL = new Nil();

    private List() {
    }

    private static class Nil<A> extends List<A> {
        private Nil() {
        }

        public A head() {
            throw new IllegalStateException("Head call on empty list");
        }

        public List<A> tail() {
            throw new IllegalStateException("Tail call on empty list");
        }

        public boolean isEmpty() {
            return true;
        }

        public List<A> setHead(A h) {
            throw new IllegalStateException("Called setHead on empty list.");
        }

        public List<A> drop(int n) {
            return this;
        }

        public List<A> reverse() {
            return this;
        }

        public List<A> dropWhile(Function<? super A, Boolean> f) {
            return this;
        }

        public List<A> removeLast() {
            throw new IllegalStateException("Remove last called on empty list.");
        }

        public <B> B foldLeft(B identity, Function<? super B, Function<? super A, ? extends B>> f) {
            throw new IllegalStateException("Fold called empty list.");
        }

        public <B> B foldRight(B identity, Function<? super A, Function<? super B, ? extends B>> f) {
            throw new IllegalStateException("Fold called empty list.");
        }

        public String toString() {
            return "[NIL]";
        }
    }

    private static class Cons<A> extends List<A> {
        private final A head;
        private final List<A> tail;

        private Cons(A head, List<A> tail) {
            this.head = head;
            this.tail = tail;
        }

        public A head() {
            return head;
        }

        public List<A> tail() {
            return tail;
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
                    : drop(this, n).eval();
        }

        public List<A> reverse() {
            List<A> acc = list();
            return reverse(acc, this).eval();
        }

        public List<A> dropWhile(Function<? super A, Boolean> f) {
            return dropWhile(this, f).eval();
        }

        public List<A> removeLast() {
            return reverse().tail().reverse();
        }

        public <B> B foldLeft(B identity, Function<? super B, Function<? super A, ? extends B>> f) {
            return foldLeft(identity, this, f).eval();
        }

        public <B> B foldRight(B identity, Function<? super A, Function<? super B, ? extends B>> f) {
            return foldRight(this, identity, f);
        }

        public String toString() {
            return String.format("[%sNIL]", toString(new StringBuilder(), this).eval());
        }

        private TailCall<List<A>> reverse(List<A> acc, List<? extends A> list) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> reverse(new Cons<>(list.head(), acc), list.tail()));
        }

        private TailCall<StringBuilder> toString(StringBuilder acc, List<? extends A> list) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> toString(acc.append(list.head()).append(", "), list.tail()));
        }

        private TailCall<List<A>> drop(List<A> list, int n) {
            return n <= 0 || list.isEmpty()
                    ? ret(list)
                    : sus(() -> drop(list.tail(), n - 1));
        }

        private TailCall<List<A>> dropWhile(List<A> list, Function<? super A, Boolean> f) {
            return !list.isEmpty() && f.apply(list.head())
                    ? sus(() -> dropWhile(list.tail(), f))
                    : ret(list);
        }

        private <B> TailCall<B> foldLeft(B acc, List<? extends A> list, Function<? super B, Function<? super A, ? extends B>> f) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> foldLeft(f.apply(acc).apply(list.head()), list.tail(), f));
        }

        private <B> B foldRight(List<? extends A> list, B identity, Function<? super A, Function<? super B, ? extends B>> f) {
            return list.isEmpty()
                    ? identity
                    : f.apply(list.head()).apply(foldRight(list.tail(), identity, f));
        }
    }
}