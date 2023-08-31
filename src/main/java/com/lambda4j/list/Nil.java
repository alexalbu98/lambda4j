package com.lambda4j.list;

import com.lambda4j.function.Function;

class Nil<A> extends List<A> {
    Nil() {
    }

    public A head() {
        throw new IllegalStateException("Head call on empty list");
    }

    public List<A> tail() {
        throw new IllegalStateException("Tail call on empty list");
    }

    public int lengthMemoized() {
        return 0;
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

    public <B> List<B> map(Function<? super A, ? extends B> f) {
        throw new IllegalStateException("Map called on empty list.");
    }

    public List<A> filter(Function<? super A, Boolean> f) {
        return this;
    }

    public <B> List<B> flatMap(Function<? super A, List<B>> f) {
        throw new IllegalStateException("Flatmap called on empty list.");
    }

    public String toString() {
        return "[NIL]";
    }
}

