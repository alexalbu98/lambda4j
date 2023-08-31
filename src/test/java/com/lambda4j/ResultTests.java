package com.lambda4j;


import com.lambda4j.function.Function;
import com.lambda4j.result.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultTests {

    @Test
    void create_works() {
        Result<String> result = Result.of("1");
        assertEquals("1", result.getOrElse("0"));

        result = Result.of("1", "message");
        assertEquals("1", result.getOrElse("0"));

        Function<String, Boolean> f = s -> s.equals("1");
        result = Result.of(f, "1", "failed");
        assertEquals("1", result.getOrElse("0"));

        result = Result.of(f, "0", "failed");
        assertEquals("0", result.getOrElse("0"));
    }

    @Test
    void map_works() {
        Result<String> result = Result.of("1");
        result = result.map(x -> x + 1);
        assertEquals("11", result.getOrElse("0"));
    }

    @Test
    void flat_map_works() {
        Result<String> result = Result.of("1");
        result = result.flatMap(x -> Result.of(x + 1));
        assertEquals("11", result.getOrElse("0"));
    }

    @Test
    void filter_works() {
        Result<String> result = Result.of("1");
        result = result.filter(x -> !x.equals("1"));
        assertEquals("0", result.getOrElse("0"));
    }
}
