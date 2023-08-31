package com.lambda4j;

import com.lambda4j.result.either.Either;
import org.junit.jupiter.api.Test;

import static com.lambda4j.result.either.Either.left;
import static com.lambda4j.result.either.Either.right;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EitherTests {
    @Test
    void create_works() {
        Either<Integer, String> either = left(1);
        assertEquals("0", either.getOrElse(() -> "0"));

        Either<Integer, String> either2 = right("1");
        assertEquals("1", either2.getOrElse(() -> "0"));
    }

    @Test
    void map_works() {
        Either<Integer, String> either = right("1");
        either = either.map(x -> x + "1");
        assertEquals("11", either.getOrElse(() -> "0"));
    }

    @Test
    void flat_map_works() {
        Either<Integer, String> either = right("1");
        either = either.flatMap(x -> left(1));
        assertEquals("0", either.getOrElse(() -> "0"));
    }

    @Test
    void or_else_works() {
        Either<Integer, String> either = left(1);
        either = either.orElse(() -> left(1));
        assertEquals("0", either.getOrElse(() -> "0"));
    }
}
