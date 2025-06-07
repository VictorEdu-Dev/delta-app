package org.deltacore.delta.controller.activity;

import java.util.Arrays;
import java.util.List;

public enum FileType {
    APPLICATION_PDF("application/pdf"),
    IMAGE_PNG("image/png"),
    IMAGE_JPEG("image/jpeg"),
    APPLICATION_DOC("application/msword"),
    APPLICATION_DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private final String fileType;

    FileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileTypeValue() {
        return fileType;
    }

    public static boolean contains(String value) {
        for (FileType type : FileType.values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
