package org.deltacore.delta.controller.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FileType {
    public static final String APPLICATION_PDF = "application/pdf";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String APPLICATION_DOC = "application/msword";
    public static final String APPLICATION_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public static final List<String> VALUES = List.of(
            APPLICATION_PDF,
            IMAGE_PNG,
            IMAGE_JPEG,
            APPLICATION_DOC,
            APPLICATION_DOCX
    );

    public static boolean contains(String value) {
        for (String type : FileType.values()) {
            if (type.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> values() {
        return VALUES;
    }
}
