package org.deltacore.delta.service.activity;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GcpConfig {
    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Value("${gcp.folder.path}")
    private String folderPath;

    @Value("${gcp.folder.path.doc}")
    private String folderPathDoc;

    @Value("${gcp.folder.path.img}")
    private String folderPathImage;
}
