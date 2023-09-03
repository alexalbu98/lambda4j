package com.lambda4j.state;

import com.lambda4j.function.Function;

interface Transition<A, S> extends Function<StateTuple<A, S>, S> {}
