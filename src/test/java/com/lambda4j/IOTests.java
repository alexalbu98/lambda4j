package com.lambda4j;

import com.lambda4j.io.IO;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class IOTests {

    @Test
    public void io_works() {
        IO<String> io = IO.from(() -> "hello,");

        io = io.map(s -> s + " world!");
        assertEquals("hello, world!", io.run());
    }
}
