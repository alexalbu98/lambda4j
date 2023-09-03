package com.lambda4j.random;

import com.lambda4j.function.Function;
import com.lambda4j.state.State;
import com.lambda4j.tuple.Tuple;

public class Random<A> extends State<RNG, A> {
    public Random(Function<RNG, Tuple<A, RNG>> run) {
        super(run);
    }

    public static State<RNG, Integer> intRnd = new Random<>(RNG::nextInt);
}