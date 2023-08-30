package com.lambda4j;

import com.lambda4j.option.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionTests {

    @Test
    void create_works() {
        Option<Integer> option = Option.some(1);
        Integer integer = option.getOrElse(() -> 0);
        assertEquals(1, integer);

        Option<Integer> option2 = Option.none();
        Integer integer2 = option2.getOrElse(() -> 0);
        assertEquals(0, integer2);
    }
}
