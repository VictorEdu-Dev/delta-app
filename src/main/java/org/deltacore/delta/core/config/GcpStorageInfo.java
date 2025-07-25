package org.deltacore.delta.core.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GcpStorageInfo {
    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Value("${gcp.folder.path}")
    private String folderPath;

    @Value("${gcp.folder.path.doc}")
    private String folderPathDoc;

    @Value("${gcp.folder.path.img}")
    private String folderPathImage;

    @Value("${gcp.folder.path.profile}")
    private String folderPathProfile;

    @Value("${gcp.folder.path.tutor.request}")
    private String folderPathTutorRequest;
}
