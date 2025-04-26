package com.erzbir.albaz.plugin.internal.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class FileTypeDetectorTest {
    String path = "plugins/plugin-test-1.0.0-all.jar";

    @Test
    void detect() throws IOException {
        FileTypeDetector.FileType detect = FileTypeDetector.detect(new File(path));
        Assertions.assertEquals(FileTypeDetector.FileType.JAR, detect);
    }

    @Test
    void testDetect() {
    }
}