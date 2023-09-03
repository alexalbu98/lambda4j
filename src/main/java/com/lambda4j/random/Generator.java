package com.lambda4j.random;

import com.lambda4j.tuple.Tuple;

public class Generator {
    public static Tuple<Integer, RNG> integer(RNG rng) {
        return rng.nextInt();
    }
}
