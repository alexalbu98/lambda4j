package com.lambda4j.list;

import com.lambda4j.function.Function;
import com.lambda4j.recursion.TailCall;
import com.lambda4j.result.Result;
import com.lambda4j.tuple.Tuple;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.lambda4j.recursion.TailCall.ret;
import static com.lambda4j.recursion.TailCall.sus;

public abstract class List<A> {
    List() {
    }

    public abstract A head();

    public abstract Result<A> headOption();

    public abstract List<A> tail();

    public abstract int lengthMemoized();

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

    public Result<A> lastOption() {
        return foldLeft(Result.empty(), x -> Result::success);
    }

    public <A1, A2> Tuple<List<A1>, List<A2>> unzip(Function<A, Tuple<A1, A2>> f) {
        return this.foldRight(new Tuple<>(list(), list()), a -> tl -> {
            Tuple<A1, A2> t = f.apply(a);
            return new Tuple<>(tl.first.append(t.first), tl.second.append(t.second));
        });
    }

    public List<A> append(A a) {
        return new Cons<>(a, this);
    }

    public int length() {
        return this.foldRight(0, x -> y -> y + 1);
    }

    public Result<A> getAt(int index) {
        return index < 0 || index >= length()
                ? Result.failure("Index out of bound")
                : getAt_(this, index).eval();
    }

    public Tuple<List<A>, List<A>> splitAt(int index) {
        return index < 0
                ? splitAt(0)
                : index > length()
                ? splitAt(length())
                : splitAt(list(), this.reverse(), this.length() - index).eval();
    }

    public boolean hasSubsequence(List<A> sublist) {
        return hasSubList(this, sublist);
    }

    public <B> Map<B, List<A>> groupBy(Function<? super A, ? extends B> f) {
        List<A> workList = this;
        Map<B, List<A>> m = new HashMap<>();
        while (!workList.isEmpty()) {
            final B k = f.apply(workList.head());
            List<A> rt = m.get(k);
            if (rt == null) rt = list();
            rt = rt.append(workList.head());
            m.put(k, rt);
            workList = workList.tail();
        }
        return Collections.unmodifiableMap(m);
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

    public static <A> List<A> flattenResult(List<Result<A>> list) {
        return flatten(list.foldRight(list(), x -> y ->
                y.append(x.map(List::list).getOrElse(list()))));
    }

    public static <A> Result<List<A>> sequence(List<Result<A>> list) {
        return list
                .filter(Result::isSuccess)
                .foldRight(Result.success(List.list()), x -> y -> Result.map2(x, y, a -> b -> new Cons<>(a, b)));
    }

    public static <A, B> B foldRight(List<? extends A> list, B identity, Function<? super A, Function<? super B, ? extends B>> f) {
        return foldRight_(identity, list.reverse(), f).eval();
    }

    public static <A, B, C> List<C> zipWith(List<A> list1, List<B> list2, Function<? super A, Function<? super B, ? extends C>> f) {
        return zipWith_(list(), list1, list2, f).eval().reverse();
    }

    public static <A> boolean hasSubList(List<A> list, List<A> sub) {
        return hasSubList_(list, sub).eval();
    }

    public static <A> Boolean startsWith(List<A> list, List<A> sub) {
        return startsWith_(list, sub).eval();
    }

    public static <A, S> List<A> unfold(S z, Function<? super S, Result<Tuple<A, S>>> f) {
        return unfold(list(), z, f).eval().reverse();
    }

    private static <A, S> TailCall<List<A>> unfold(List<A> acc, S z, Function<? super S, Result<Tuple<A, S>>> f) {
        Result<Tuple<A, S>> r = f.apply(z);
        Result<TailCall<List<A>>> result =
                r.map(rt -> sus(() -> unfold(acc.append(rt.first), rt.second, f)));
        return result.getOrElse(ret(acc));
    }

    private static <A> TailCall<Boolean> hasSubList_(List<A> list, List<A> sub) {
        return list.isEmpty()
                ? ret(sub.isEmpty())
                : startsWith(list, sub)
                ? ret(true)
                : sus(() -> hasSubList_(list.tail(), sub));
    }

    private static <A> TailCall<Boolean> startsWith_(List<A> list,
                                                     List<A> sub) {
        return sub.isEmpty()
                ? ret(Boolean.TRUE)
                : list.isEmpty()
                ? ret(Boolean.FALSE)
                : list.head().equals(sub.head())
                ? sus(() -> startsWith_(list.tail(), sub.tail()))
                : ret(Boolean.FALSE);
    }

    private TailCall<Tuple<List<A>, List<A>>> splitAt(List<A> acc,
                                                      List<A> list, int i) {
        return i == 0 || list.isEmpty()
                ? ret(new Tuple<>(list.reverse(), acc))
                : sus(() -> splitAt(acc.append(list.head()), list.tail(), i - 1));
    }

    private static <A> TailCall<Result<A>> getAt_(List<A> list, int index) {
        return index == 0
                ? TailCall.ret(Result.success(list.head()))
                : TailCall.sus(() -> getAt_(list.tail(), index - 1));
    }

    private static <A, B, C> TailCall<List<C>> zipWith_(List<C> acc, List<A> list1, List<B> list2, Function<? super A, Function<? super B, ? extends C>> f) {
        return list1.isEmpty() || list2.isEmpty()
                ? ret(acc)
                : sus(() -> zipWith_(
                new Cons<>(f.apply(list1.head()).apply(list2.head()), acc),
                list1.tail(), list2.tail(), f));
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