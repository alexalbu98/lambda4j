package com.lambda4j.state;

import com.lambda4j.function.Function;

interface Condition<I, S> extends Function<StateTuple<I, S>, Boolean> {}