package com.lambda4j;

import com.lambda4j.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.lambda4j.control.Case.match;
import static com.lambda4j.control.Case.mcase;
import static com.lambda4j.result.Result.failure;
import static com.lambda4j.result.Result.success;
import static org.junit.jupiter.api.Assertions.*;

class CaseTests {

    @Test
    void case_works() {
        Result<String> result1 = verifyString("test");
        result1.bind(string -> assertEquals("test", string), Assertions::assertNull);

        Result<String> result2 = verifyString(null);
        result2.bind(Assertions::assertNull, Assertions::assertNotNull);

        Result<String> result3 = verifyString("");
        result3.bind(Assertions::assertNull, Assertions::assertNotNull);
    }

    private Result<String> verifyString(String s) {
        return match(
                mcase(() -> success(s)),
                mcase(() -> s == null, () -> failure("string must not be null")),
                mcase(() -> s.equals(""), () -> failure("string must not be empty"))
        );
    }
}
