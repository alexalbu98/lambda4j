package com.lambda4j.random;

import com.lambda4j.state.StateTuple;

import java.util.Random;

public class JavaRNG implements RNG {

    private final Random random;

    public JavaRNG(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public StateTuple<Integer, RNG> nextInt() {
        return new StateTuple<>(random.nextInt(), this);
    }

    public static RNG rng(long seed) {
        return new JavaRNG(seed);
    }

}
