package com.lambda4j;

import com.lambda4j.list.List;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static com.lambda4j.list.List.*;
import static org.junit.jupiter.api.Assertions.*;

class ListTests {

    @Test
    void create_list_works() {
        List<Object> list = list();
        assertTrue(list.isEmpty());

        List<Integer> integerList = list(1, 2, 3);
        assertFalse(integerList.isEmpty());
        assertEquals(3, integerList.length());
        assertEquals(3, integerList.lengthMemoized());
    }

    @Test
    void concatWorks() {
        List<String> list1 = list("test", "test1", "test3");
        List<String> list2 = list("test3", "test4", "test5");
        List<String> list = concat(list1, list2);
        assertEquals(6, list.length());
        assertEquals("test", list.head());
    }

    @Test
    void set_head_works() {
        List<String> list = list("test", "test1", "test3");
        list = list.setHead("one");
        assertEquals("one", list.head());
    }

    @Test
    void drop_works() {
        List<String> list = list("test", "test1", "test3");
        list = list.drop(2);
        assertEquals("test3", list.head());
    }

    @Test
    void drop_while_works() {
        List<String> list = list("test", "test1", "test3", "one");
        list = list.dropWhile(s -> s.contains("test"));
        assertEquals("one", list.head());
    }

    @Test
    void reverse_works() {
        List<String> list = list("test", "test1", "test3", "one");
        list = list.reverse();
        assertEquals("one", list.head());
    }

    @Test
    void remove_last_works() {
        List<String> list = list("test", "test1", "test3", "one");
        list = list.removeLast();
        assertEquals("test3", list.tail().tail().head());
    }

    @Test
    void fold_works() {
        List<String> list = list("hello", "world");

        String left = list.foldLeft("", x -> y -> x + y);
        assertEquals("helloworld", left);

        String right = list.foldRight("", x -> y -> y + x);
        assertEquals("worldhello", right);

        String right2 = foldRight(list, "", x -> y -> y + x);
        assertEquals("worldhello", right2);
    }

    @Test
    void map_works() {
        List<String> list = list("hello", "world");

        List<String> map = list.map(s -> s + "1");
        assertEquals("hello1", map.head());
    }

    @Test
    void filer_works() {
        List<String> list = list("one", "one", "one", "test");
        list = list.filter(s -> s.contains("test"));
        assertEquals(1, list.length());
        assertEquals("test", list.head());
    }

    @Test
    void flatmap_works() {
        List<String> list = list("one", "one");
        List<Character> chars = list.flatMap(this::convertStringToChars);
        assertEquals(6, chars.length());
        assertEquals('o', chars.head());
    }

    @Test
    void flatten_works() {
        List<List<String>> lists = list(list("one"), list("test"));
        List<String> list = flatten(lists);

        assertEquals(2, list.length());
        assertEquals("one", list.head());

    }

    private List<Character> convertStringToChars(String inputString) {
        java.util.List<Character> chars = inputString.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        return list(chars);
    }

}
