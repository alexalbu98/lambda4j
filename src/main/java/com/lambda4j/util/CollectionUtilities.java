package com.lambda4j.util;

import com.lambda4j.effect.Effect;
import com.lambda4j.function.Function;

import java.util.*;

public class CollectionUtilities {
    private CollectionUtilities() {
    }

    public static <T> List<T> list() {
        return Collections.emptyList();
    }

    public static <T> List<T> list(T t) {
        return Collections.singletonList(t);
    }

    public static <T> List<T> list(List<T> ts) {
        return Collections.unmodifiableList(new ArrayList<>(ts));
    }

    @SafeVarargs
    public static <T> List<T> list(T... t) {
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(t, t.length)));
    }

    public static <T> T head(List<? extends T> list) {
        if (list.isEmpty()) {
            throw new IllegalStateException("head of empty list");
        }
        return list.get(0);
    }

    public static <T> List<T> tail(List<? extends T> list) {
        if (list.isEmpty()) {
            throw new IllegalStateException("tail of empty list");
        }
        List<T> workList = copy(list);
        workList.remove(0);
        return Collections.unmodifiableList(workList);
    }

    public static <T> List<T> reverse(List<? extends T> list) {
        List<T> result = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            result.add(list.get(i));
        }
        return Collections.unmodifiableList(result);
    }

    public static <T> List<T> append(List<? extends T> list, T t) {
        List<T> ts = copy(list);
        ts.add(t);
        return Collections.unmodifiableList(ts);
    }

    public <T, U> List<U> map(List<? extends T> list, Function<? super T, ? extends U> f) {
        List<U> newList = new ArrayList<>();
        for (T value : list) {
            newList.add(f.apply(value));
        }
        return Collections.unmodifiableList(newList);
    }

    public static <T, U> U foldLeft(List<? extends T> ts, U identity, Function<? super U, Function<? super T, ? extends U>> f) {
        U result = identity;
        for (T t : ts) {
            result = f.apply(result).apply(t);
        }
        return result;
    }

    public static <T, U> U foldRight(List<? extends T> ts, U identity, Function<? super T, Function<? super U, ? extends U>> f) {
        U result = identity;
        for (int i = ts.size(); i > 0; i--) {
            result = f.apply(ts.get(i - 1)).apply(result);
        }
        return result;
    }

    public static <T> List<T> unfold(T seed, Function<? super T, ? extends T> producer, Function<? super T, Boolean> condition) {
        List<T> result = new ArrayList<>();
        T temp = seed;
        while (condition.apply(temp)) {
            result = append(result, temp);
            temp = producer.apply(temp);
        }
        return Collections.unmodifiableList(result);
    }

    public static <T> void forEach(Collection<? extends T> ts, Effect<? super T> e) {
        for (T t : ts) e.apply(t);
    }

    private static <T> List<T> copy(List<? extends T> ts) {
        return new ArrayList<>(ts);
    }
}
