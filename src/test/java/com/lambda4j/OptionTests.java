package com.lambda4j;

import com.lambda4j.option.Option;
import org.junit.jupiter.api.Test;

import static com.lambda4j.option.Option.none;
import static com.lambda4j.option.Option.some;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionTests {

    @Test
    void create_works() {
        Option<Integer> option = some(1);
        Integer integer = option.getOrElse(() -> 0);
        assertEquals(1, integer);

        Option<Integer> option2 = Option.none();
        Integer integer2 = option2.getOrElse(() -> 0);
        assertEquals(0, integer2);
    }

    @Test
    void map_works() {
        Option<Integer> option = some(1);
        option = option.map(i -> i + 1);
        assertEquals(2, option.getOrElse(() -> 0));
    }

    @Test
    void flatmap_works() {
        Option<Integer> option = some(1);
        option = option.flatMap(i -> some(i + 1));
        assertEquals(2, option.getOrElse(() -> 0));
    }

    @Test
    void or_else_works() {
        Option<Integer> option = none();
        option = option.orElse(() -> some(2));
        assertEquals(2, option.getOrElse(() -> 0));
    }

    @Test
    void filter_works() {
        Option<Integer> option = some(1);
        option = option.filter(x -> x != 1);
        assertEquals(0, option.getOrElse(() -> 0));
    }
}
