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

    public abstract List<A> dropWhile(Function<A, Boolean> f);

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

        public List<A> dropWhile(Function<A, Boolean> f) {
            return this;
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

        public List<A> dropWhile(Function<A, Boolean> f) {
            return dropWhile(this, f).eval();
        }

        public String toString() {
            return String.format("[%sNIL]", toString(new StringBuilder(), this).eval());
        }

        private TailCall<StringBuilder> toString(StringBuilder acc, List<A> list) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> toString(acc.append(list.head()).append(", "), list.tail()));
        }

        private TailCall<List<A>> drop(List<A> list, int n) {
            return n <= 0 || list.isEmpty()
                    ? ret(list)
                    : sus(() -> drop(list.tail(), n - 1));
        }

        private TailCall<List<A>> dropWhile(List<A> list, Function<A, Boolean> f) {
            return !list.isEmpty() && f.apply(list.head())
                    ? sus(() -> dropWhile(list.tail(), f))
                    : ret(list);
        }
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

    public List<A> cons(A a) {
        return new Cons<>(a, this);
    }

}