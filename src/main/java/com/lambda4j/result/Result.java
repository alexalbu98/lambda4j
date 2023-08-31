package com.lambda4j.result;


import com.lambda4j.effect.Effect;
import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;

import java.io.Serializable;

public abstract class Result<V> implements Serializable {
    Result() {
    }

    public abstract boolean isSuccess();

    public abstract boolean isFailure();

    public abstract V getOrElse(final V defaultValue);

    public abstract V getOrElse(final Supplier<? extends V> defaultValue);

    public abstract <U> Result<U> map(Function<? super V, ? extends U> f);

    public abstract <U> Result<U> flatMap(Function<? super V, Result<U>> f);

    public abstract Result<V> mapFailure(String s);

    public abstract void forEach(Effect<V> ef);

    public abstract void forEachOrThrow(Effect<V> ef);

    public abstract Result<RuntimeException> forEachOrException(Effect<V> ef);


    public Result<V> orElse(Supplier<Result<V>> defaultValue) {
        return map(x -> this).getOrElse(defaultValue);
    }

    public Result<V> filter(Function<? super V, Boolean> p) {
        return flatMap(x -> p.apply(x)
                ? this
                : failure("Condition not matched"));
    }

    public Result<V> filter(Function<? super V, Boolean> p, String message) {
        return flatMap(x -> p.apply(x)
                ? this
                : failure(message));
    }

    public boolean exists(Function<? super V, Boolean> p) {
        return map(p).getOrElse(false);
    }

    public static <V> Result<V> failure(String message) {
        return new Failure<>(message);
    }

    public static <V> Result<V> failure(Exception e) {
        return new Failure<>(e);
    }

    public static <V> Result<V> failure(RuntimeException e) {
        return new Failure<>(e);
    }

    public static <V> Result<V> success(V value) {
        return new Success<>(value);
    }

    public static <V> Result<V> empty() {
        return new Empty<>();
    }

    public static <T> Result<T> of(T value) {
        return value != null
                ? success(value)
                : Result.failure("Null value");
    }

    public static <T> Result<T> of(T value, String message) {
        return value != null
                ? success(value)
                : failure(message);
    }

    public static <T> Result<T> of(Function<? super T, Boolean> predicate, T value, String message) {
        try {
            return predicate.apply(value)
                    ? Result.success(value)
                    : Result.failure(String.format(message, value));
        } catch (Exception e) {
            String errMessage = String.format("Exception while evaluating predicate: %s", String.format(message, value));
            return Result.failure(new IllegalStateException(errMessage, e));
        }
    }

    public static <A, B> Function<Result<A>, Result<B>> lift(final Function<? super A, ? extends B> f) {
        return x -> {
            try {
                return x.map(f);
            } catch (Exception e) {
                return failure(e);
            }
        };
    }

    public static <A, B> Result<B> map(Result<A> a, Function<A, B> f) {
        return lift(f).apply(a);
    }

    public static <A, B, C> Result<C> map2(Result<A> a, Result<B> b, Function<A, Function<? super B, ? extends C>> f) {
        return a.flatMap(x -> b.map(y -> f.apply(x).apply(y)));
    }
}