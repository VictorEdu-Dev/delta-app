package org.deltacore.delta.service.activity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GcpConfig {
    private static String BUCKET_NAME;
    private static String FOLDER_PATH;
    private static String FOLDER_PATH_DOC;
    private static String FOLDER_PATH_IMAGE;

    @Value("${gcp.bucket.name}")
    public void setBucketName(String bucketName) {
        GcpConfig.BUCKET_NAME = bucketName;
    }

    @Value("${gcp.folder.path}")
    public void setFolderPath(String folderPath) {
        GcpConfig.FOLDER_PATH = folderPath;
    }

    @Value("${gcp.folder.path.doc}")
    public void setFolderPathDoc(String folderPathDoc) {
        GcpConfig.FOLDER_PATH_DOC = folderPathDoc;
    }

    @Value("${gcp.folder.path.img}")
    public void setFolderPathImage(String folderPathImage) {
        GcpConfig.FOLDER_PATH_IMAGE = folderPathImage;
    }

    public static String getBucketName() {
        return BUCKET_NAME;
    }

    public static String getFolderPath() {
        return FOLDER_PATH;
    }

    public static String getFolderPathDoc() {
        return FOLDER_PATH_DOC;
    }

    public static String getFolderPathImage() {
        return FOLDER_PATH_IMAGE;
    }
}
