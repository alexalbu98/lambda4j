package com.lambda4j.state;

import com.lambda4j.function.Function;
import com.lambda4j.list.List;

public class State<S, A> {
    public final Function<S, StateTuple<A, S>> run;

    public State(Function<S, StateTuple<A, S>> run) {
        this.run = run;
    }

    public static <S, A> State<S, A> unit(A a) {
        return new State<>(state -> new StateTuple<>(a, state));
    }

    public <B> State<S, B> map(Function<A, B> f) {
        return flatMap(a -> State.unit(f.apply(a)));
    }

    public <B, C> State<S, C> map2(State<S, B> sb, Function<A, Function<B, C>> f) {
        return flatMap(a -> sb.map(b -> f.apply(a).apply(b)));
    }

    public <B> State<S, B> flatMap(Function<A, State<S, B>> f) {
        return new State<>(s -> {
            StateTuple<A, S> temp = run.apply(s);
            return f.apply(temp.value)
                    .run.apply(temp.state);
        });
    }

    public static <S, A> State<S, List<A>> sequence(List<State<S, A>> fs) {
        return fs.foldRight(State.unit(List.list()),
                f -> acc -> f.map2(acc, a -> b -> b.append(a)));
    }

    public static <S> State<S, Nothing> sequence(Function<S, S> f) {
        return new State<>(s -> new StateTuple<>(Nothing.instance, f.apply(s)));
    }

    public static <S> State<S, S> get() {
        return new State<>(s -> new StateTuple<>(s, s));
    }

    public static <S> State<S, Nothing> set(S s) {
        return new State<>(x -> new StateTuple<>(Nothing.instance, s));
    }

    public static <S, A> State<S, List<A>> compose(List<State<S, A>> fs) {
        return fs.foldRight(State.unit(List.list()),
                f -> acc -> f.map2(acc, a -> b -> b.append(a)));
    }

    public static <S> State<S, Nothing> modify(Function<S, S> f) {
        return State.<S>get().flatMap(s -> set(f.apply(s)));
    }
}