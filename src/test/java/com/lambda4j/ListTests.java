package com.lambda4j;

import com.lambda4j.list.List;
import org.junit.jupiter.api.Test;

import static com.lambda4j.list.List.list;
import static org.junit.jupiter.api.Assertions.*;

class ListTests {

    @Test
    void create_list_works() {
        List<Object> list = list();
        assertTrue(list.isEmpty());

        List<Integer> integerList = list(1, 2, 3);
        assertFalse(integerList.isEmpty());
        assertEquals(3, integerList.length());
    }
}
