package com.lambda4j;

import com.lambda4j.list.List;
import com.lambda4j.result.Result;
import com.lambda4j.tuple.Tuple;
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

    @Test
    void flatten_result_works() {
        List<Result<String>> list = list(Result.success("test1"), Result.success("test2"), Result.failure("test3"));
        List<String> flattened = flattenResult(list);

        assertEquals(2, flattened.length());
        assertEquals("test1", flattened.head());
    }

    @Test
    void option_works() {
        List<String> list = list("one", "two", "three");

        Result<String> result = list.headOption();
        assertEquals("one", result.getOrElse("0"));

        result = list.lastOption();
        assertEquals("three", result.getOrElse("0"));

    }

    @Test
    void sequence_works() {
        List<Result<String>> resultList = list(Result.success("test1"), Result.success("test2"), Result.failure("test3"), Result.empty());
        Result<List<String>> result = sequence(resultList);
        List<String> list = result.getOrElse(list());
        assertEquals("test1", list.head());
        assertEquals(2, list.lengthMemoized());
    }

    @Test
    void zip_with_works() {
        List<String> list1 = list("one", "two", "three");
        List<String> list2 = list(" one", " two", " three");
        List<String> result = zipWith(list1, list2, x -> y -> x + y);
        Tuple<List<String>, List<String>> listTuple = result.unzip(x -> new Tuple<>(x.split(" ")[0], x.split(" ")[1]));
        assertEquals("one one", result.head());
        assertEquals(3, result.lengthMemoized());
        assertEquals(list1.toString(), listTuple.first.toString());
        assertEquals(list2.map(String::trim).toString(), listTuple.second.toString());
    }

    @Test
    void get_at_works() {
        List<String> list = list("one", "two", "three");
        assertEquals("one", list.getAt(0).getOrElse(""));
        assertEquals("three", list.getAt(2).getOrElse(""));
    }

    @Test
    void split_at_works() {
        List<String> list = list("one", "two", "three", "four");
        assertEquals("[one, two, NIL]", list.splitAt(2).first.toString());
        assertEquals("[three, four, NIL]", list.splitAt(2).second.toString());
    }

    @Test
    void has_sublist_works() {
        List<String> list1 = list("one", "two", "three", "four");
        List<String> list2 = list("one", "two");
        List<String> list3 = list("two", "three");
        assertTrue(hasSubList(list1, list2));
        assertTrue(hasSubList(list1, list3));
        assertTrue(list1.hasSubsequence(list2));
        assertTrue(list1.hasSubsequence(list3));
    }

    private List<Character> convertStringToChars(String inputString) {
        java.util.List<Character> chars = inputString.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        return list(chars);
    }

}
