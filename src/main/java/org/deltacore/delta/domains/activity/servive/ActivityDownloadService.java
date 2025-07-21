package org.deltacore.delta.domains.activity.servive;

import com.google.cloud.storage.*;
import org.deltacore.delta.core.config.GcpStorageInfo;
import org.deltacore.delta.domains.activity.model.ActivityFiles;
import org.deltacore.delta.domains.activity.repository.ActivityFilesDAO;
import org.deltacore.delta.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class ActivityDownloadService {
    private final Storage storage;
    private GcpStorageInfo gcpStorageInfo;
    private ActivityFilesDAO activityFilesDAO;
    private MessageSource messageSource;

    @Autowired
    public ActivityDownloadService(Storage storage) {
        this.storage = storage;
    }

    private byte[] downloadFile(String objectName) {
        Blob blob = storage.get(BlobId.of(gcpStorageInfo.getBucketName(), objectName));
        if (blob == null || !blob.exists()) {
            throw new ResourceNotFoundException("File not found in bucket: " + objectName);
        }

        try {
            return blob.getContent();
        } catch (StorageException e) {
            throw new RuntimeException("Failed to download file: " + objectName, e);
        }
    }

    public byte[] loadActivityFile(Long activityFileId) {
        ActivityFiles activityFile = activityFilesDAO.findById(activityFileId)
                .orElseThrow(() -> {
                    String msg = getMessage("error.file.not.found");
                    return new ResourceNotFoundException(msg);
                });

        String objectName = activityFile.getFilePath() + activityFile.getFileName();

        return downloadFile(objectName);
    }

    public String getObjectNameByFileId(Long activityFileId) {
        ActivityFiles activityFile = activityFilesDAO.findById(activityFileId)
                .orElseThrow(() -> {
                    String msg = getMessage("error.file.not.found");
                    return new ResourceNotFoundException(msg);
                });
        return activityFile.getFilePath() + activityFile.getFileName();
    }

    public URL getSignedUrlByFileId(Long activityFileId) throws Exception {
        ActivityFiles activityFile = activityFilesDAO.findById(activityFileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + activityFileId));

        String objectName = activityFile.getFilePath() + activityFile.getFileName();

        return generateSignedUrl(
                gcpStorageInfo.getBucketName(),
                objectName
        );
    }

    private URL generateSignedUrl(
            String bucketName,
            String objectName) {

        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();

        Storage.SignUrlOption[] options = new Storage.SignUrlOption[]{
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.httpMethod(HttpMethod.GET)
        };

        return storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, options);
    }


    private String getMessage(String local) {
        return messageSource.getMessage(local, null, LocaleContextHolder.getLocale());
    }

    @Autowired @Lazy
    private void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    private void setActivityFilesDAO(ActivityFilesDAO activityFilesDAO) {
        this.activityFilesDAO = activityFilesDAO;
    }

    @Autowired
    private void setGcpStorageInfo(GcpStorageInfo gcpStorageInfo) {
        this.gcpStorageInfo = gcpStorageInfo;
    }
}
