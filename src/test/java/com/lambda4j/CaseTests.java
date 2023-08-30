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
        assertEquals("test", result1.getOrElse(() -> ""));

        Result<String> result2 = verifyString(null);
        assertEquals("", result2.getOrElse(() -> ""));

        Result<String> result3 = verifyString("");
        assertEquals("", result3.getOrElse(() -> ""));
    }

    private Result<String> verifyString(String s) {
        return match(
                mcase(() -> success(s)),
                mcase(() -> s == null, () -> failure("string must not be null")),
                mcase(() -> s.equals(""), () -> failure("string must not be empty"))
        );
    }
}
