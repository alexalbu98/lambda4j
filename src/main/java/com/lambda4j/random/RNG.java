package com.lambda4j.random;

import com.lambda4j.tuple.Tuple;

public interface RNG {
    Tuple<Integer, RNG> nextInt();
}
