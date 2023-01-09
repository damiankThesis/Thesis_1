package com.praca.komis.project.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ChangeNameExistFileUtilTest {

    @Test
    @SneakyThrows
    void shouldNotRenameFilename(@TempDir Path tempDir) {
        //given
        String newFilename = ChangeNameExistFileUtil.renameIfExist(tempDir, "test.png");

        //then
        assertEquals("test.png", newFilename);
    }

    @Test
    @SneakyThrows
    void shouldRenameFilenameIfExists(@TempDir Path tempDir) {
        Files.createFile(tempDir.resolve("test.png"));

        //given
        String newFilename = ChangeNameExistFileUtil.renameIfExist(tempDir, "test.png");

        //then
        assertEquals("test-1.png", newFilename);
    }

    @Test
    @SneakyThrows
    void shouldRenameMultipleTimesFilenameIfExists(@TempDir Path tempDir) {
        Files.createFile(tempDir.resolve("test.png"));
        Files.createFile(tempDir.resolve("test-1.png"));
        Files.createFile(tempDir.resolve("test-2.png"));

        //given
        String newFilename = ChangeNameExistFileUtil.renameIfExist(tempDir, "test.png");

        //then
        assertEquals("test-3.png", newFilename);
    }

}
