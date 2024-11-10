package com.erzbir.albaz.util;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class XmlUtil {
    public static Document readXML(@NotNull File file) {
        if (!file.exists()) {
            throw new RuntimeException("File [" + file.getAbsolutePath() + "] not a exist!");
        } else if (!file.isFile()) {
            throw new RuntimeException("[" + file.getAbsolutePath() + "] not a file!");
        } else {
            try {
                file = file.getCanonicalFile();
            } catch (IOException ignored) {
            }

            BufferedInputStream in;

            Document document;
            try {
                in = new BufferedInputStream(new FileInputStream(file));
                document = readXML(in);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return document;
        }
    }

    public static Document readXML(InputStream in) {
        Document document;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            document = builder.parse(in);
        } catch (Exception e) {
            throw new RuntimeException("Parse XML from stream error!");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {

                }
            }
        }
        return document;
    }
}
