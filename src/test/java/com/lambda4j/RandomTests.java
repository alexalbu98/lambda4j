package com.lambda4j;

import com.lambda4j.random.Generator;
import com.lambda4j.random.JavaRNG;
import com.lambda4j.random.RNG;
import com.lambda4j.state.StateTuple;
import com.lambda4j.tuple.Tuple;
import org.junit.jupiter.api.Test;

public class RandomTests {

    @Test
    void test_integer() {
        RNG rng = new JavaRNG(42);
        StateTuple<Integer, RNG> t1 = Generator.integer(rng);
        StateTuple<Integer, RNG> t2 = Generator.integer(t1.state);
        StateTuple<Integer, RNG> t3 = Generator.integer(t2.state);
    }
}
