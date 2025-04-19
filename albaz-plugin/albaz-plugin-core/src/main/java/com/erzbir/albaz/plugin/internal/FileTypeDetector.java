package com.erzbir.albaz.plugin.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileTypeDetector {

    public static FileType detect(File file) throws IOException {
        return detect(new FileInputStream(file));
    }

    public static FileType detect(InputStream in) throws IOException {
        byte[] header = getFileHeader(in);
        return detect(header);
    }

    private static FileType detect(byte[] header) {
        if (isClass(header)) {
            return FileType.CLASS;
        }
        if (isJar(header)) {
            return FileType.JAR;
        }
        return FileType.UNKNOWN;
    }

    private static byte[] getFileHeader(InputStream in) throws IOException {
        byte[] header = new byte[4];
        int read = in.read(header);
        in.close();
        if (read < 4) {
            return new byte[]{0, 0, 0, 0};
        }
        return header;
    }

    public static boolean isClass(byte[] header) {
        return (header[0] & 0xFF) == 0xCA
                && (header[1] & 0xFF) == 0xFE
                && (header[2] & 0xFF) == 0xBA
                && (header[3] & 0xFF) == 0xBE;
    }

    public static boolean isJar(byte[] header) {
        return ((header[0] & 0xFF) == 0x50
                && (header[1] & 0xFF) == 0x4B
                && (header[2] & 0xFF) == 0x03
                && (header[3] & 0xFF) == 0x04);
    }

    public enum FileType {
        CLASS,
        JAR,
        UNKNOWN
    }
}