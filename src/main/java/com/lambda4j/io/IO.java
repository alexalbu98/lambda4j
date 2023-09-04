package com.lambda4j.io;

import com.lambda4j.function.Function;
import com.lambda4j.function.Supplier;
import com.lambda4j.recursion.TailCall;
import com.lambda4j.state.Nothing;

import static com.lambda4j.recursion.TailCall.ret;
import static com.lambda4j.recursion.TailCall.sus;

public abstract class IO<A> {

    protected abstract boolean isReturn();

    protected abstract boolean isSuspend();

    public static IO<Nothing> empty() {
        return new IO.Suspend<>(() -> Nothing.instance);
    }

    public static <A> IO<A> from(Supplier<A> ioOpr) {
        return new IO.Suspend<>(ioOpr);
    }

    public A run() {
        return run(this);
    }

    public A run(IO<A> io) {
        return run_(io).eval();
    }

    private TailCall<A> run_(IO<A> io) {
        if (io.isReturn()) {
            return ret(((Return<A>) io).value);
        } else if (io.isSuspend()) {
            return ret(((Suspend<A>) io).resume.get());
        } else {
            Continue<A, A> ct = (Continue<A, A>) io;
            IO<A> sub = ct.sub;
            Function<A, IO<A>> f = ct.f;
            if (sub.isReturn()) {
                return sus(() -> run_(f.apply(((Return<A>) sub).value)));
            } else if (sub.isSuspend()) {
                return sus(() -> run_(f.apply(((Suspend<A>) sub).resume.get())));
            } else {
                Continue<A, A> ct2 = (Continue<A, A>) sub;
                IO<A> sub2 = ct2.sub;
                Function<A, IO<A>> f2 = ct2.f;
                return sus(() -> run_(sub2.flatMap(x ->
                        f2.apply(x).flatMap(f))));
            }
        }
    }

    public <B> IO<B> map(Function<A, B> f) {
        return flatMap(f.andThen(Return::new));
    }

    @SuppressWarnings("unchecked")
    public <B> IO<B> flatMap(Function<A, IO<B>> f) {
        return (IO<B>) new Continue<>(this, f);
    }

    static <A> IO<A> unit(A a) {
        return new IO.Suspend<>(() -> a);
    }

    final static class Return<T> extends IO<T> {
        public final T value;

        private Return(T value) {
            this.value = value;
        }

        @Override
        public boolean isReturn() {
            return true;
        }

        @Override
        public boolean isSuspend() {
            return false;
        }

    }

    final static class Suspend<T> extends IO<T> {
        public final Supplier<T> resume;

        private Suspend(Supplier<T> resume) {
            this.resume = resume;
        }

        @Override
        public boolean isReturn() {
            return false;
        }

        @Override
        public boolean isSuspend() {
            return true;
        }
    }

    final static class Continue<T, U> extends IO<T> {
        public final IO<T> sub;
        public final Function<T, IO<U>> f;

        private Continue(IO<T> sub, Function<T, IO<U>> f) {
            this.sub = sub;
            this.f = f;
        }

        @Override
        public boolean isReturn() {
            return false;
        }

        @Override
        public boolean isSuspend() {
            return false;
        }
    }
}