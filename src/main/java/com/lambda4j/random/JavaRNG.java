package com.lambda4j.random;

import com.lambda4j.tuple.Tuple;

import java.util.Random;

public class JavaRNG implements RNG {

    private final Random random;

    public JavaRNG(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public Tuple<Integer, RNG> nextInt() {
        return new Tuple<>(random.nextInt(), this);
    }

    public static RNG rng(long seed) {
        return new JavaRNG(seed);
    }

}
