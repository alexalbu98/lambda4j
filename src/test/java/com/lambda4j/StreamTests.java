package com.lambda4j;

import com.lambda4j.list.List;
import com.lambda4j.list.stream.Stream;
import org.junit.jupiter.api.Test;

import static com.lambda4j.list.List.list;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StreamTests {

    @Test
    void create_stream_works() {
        Stream<Integer> stream = Stream.empty();
        assertTrue(stream.isEmpty());

        stream = list(1, 2, 3, 4).toStream();
        assertTrue(stream.exists(x -> x == 1));
        assertTrue(stream.exists(x -> x == 2));
        assertTrue(stream.exists(x -> x == 3));
        assertTrue(stream.exists(x -> x == 4));
    }

    @Test
    void map_works() {
        Stream<Integer> stream = list(1, 2, 3, 4).toStream();
        stream = stream.map(x -> x + 1);
        assertTrue(stream.exists(x -> x == 2));
        assertTrue(stream.exists(x -> x == 3));
        assertTrue(stream.exists(x -> x == 4));
        assertTrue(stream.exists(x -> x == 5));
    }

    @Test
    void flat_map_works() {
        Stream<String> stream = list("test").toStream();
        Stream<Character> characterStream = stream.flatMap(x -> toList(x.toCharArray()).toStream());
        assertTrue(characterStream.exists(x -> x == 't'));
        assertTrue(characterStream.exists(x -> x == 'e'));
        assertTrue(characterStream.exists(x -> x == 's'));
        assertTrue(characterStream.exists(x -> x == 't'));
    }

    @Test
    void fold_right_works() {
        Stream<Integer> stream = list(1, 2, 3, 4).toStream();
        int result = stream.foldRight(() -> 0, x -> y -> x + y.get());
        assertEquals(10, result);
    }

    @Test
    void fold_left_works() {
        Stream<String> stream = list("hello", "world").toStream();
        String result = stream.foldLeft(() -> "", x -> y -> x.get() + y);
        assertEquals("worldhello", result);
    }

    private List<Character> toList(char[] characters) {
        List<Character> charList = list();
        for (char c : characters) {
            charList = charList.append(c);
        }
        return charList;
    }

}
