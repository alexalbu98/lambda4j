package com.lambda4j.random;

import com.lambda4j.state.StateTuple;
import com.lambda4j.tuple.Tuple;

public interface RNG {
    StateTuple<Integer, RNG> nextInt();
}
