package com.praca.komis.project.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UploadImageUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "test test.png, test-test.png",
            "ąśćńź test.png, ascnz-test.png",
            "Test 12.png, test-12.png",
            "Test - ą12.png, test-a12.png"
    })
    void shouldSlugifyFilenameCorrect(String in, String out) {
        //given
        String name = UploadImageUtils.slugifyFilename(in);

        //then
        assertEquals(name, out);
    }

}
