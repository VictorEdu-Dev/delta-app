package org.deltacore.delta.service.activity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GcpConfig {
    private String BUCKET_NAME;
    private String FOLDER_PATH;
    private String FOLDER_PATH_DOC;
    private String FOLDER_PATH_IMAGE;

    @Value("${gcp.bucket.name}")
    public void setBucketName(String bucketName) {
        this.BUCKET_NAME = bucketName;
    }

    @Value("${gcp.folder.path}")
    public void setFolderPath(String folderPath) {
        this.FOLDER_PATH = folderPath;
    }

    @Value("${gcp.folder.path.doc}")
    public void setFolderPathDoc(String folderPathDoc) {
        this.FOLDER_PATH_DOC = folderPathDoc;
    }

    @Value("${gcp.folder.path.img}")
    public void setFolderPathImage(String folderPathImage) {
        this.FOLDER_PATH_IMAGE = folderPathImage;
    }

    public String getBucketName() {
        return BUCKET_NAME;
    }

    public String getFolderPath() {
        return FOLDER_PATH;
    }

    public String getFolderPathDoc() {
        return FOLDER_PATH_DOC;
    }

    public String getFolderPathImage() {
        return FOLDER_PATH_IMAGE;
    }
}
