package io.github.avew.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ImageUtil {

    public static void base64ToImage(File output, String content) throws IOException {
        byte[] data = DatatypeConverter.parseBase64Binary(content);
        FileUtils.writeByteArrayToFile(output, data);
    }

    private static String fileToBase64(File file) throws IOException {
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }
}
