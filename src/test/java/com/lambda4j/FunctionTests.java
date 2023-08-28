package com.lambda4j;


import com.lambda4j.function.Function;
import org.junit.jupiter.api.Test;

import static com.lambda4j.function.Function.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionTests {

    @Test
    void compose_works() {
        Function<Integer, Integer> increment = x -> x + 1;
        Function<Integer, Integer> square = x -> x * x;

        assertEquals(10, increment.compose(square).apply(3));
        assertEquals(10, compose(increment, square).apply(3));
        assertEquals(10, Function.<Integer, Integer, Integer>compose().apply(square).apply(increment).apply(3));
        assertEquals(10, Function.<Integer, Integer, Integer>higherCompose().apply(increment).apply(square).apply(3));
    }

    @Test
    void then_works() {
        Function<Integer, Integer> increment = x -> x + 1;
        Function<Integer, Integer> square = x -> x * x;

        assertEquals(16, increment.andThen(square).apply(3));
        assertEquals(16, andThen(increment, square).apply(3));
        assertEquals(16, Function.<Integer, Integer, Integer>andThen().apply(square).apply(increment).apply(3));
        assertEquals(16, Function.<Integer, Integer, Integer>higherAndThen().apply(increment).apply(square).apply(3));
    }

    @Test
    void identity_works() {
        assertEquals(1, identity().apply(1));
    }
}
