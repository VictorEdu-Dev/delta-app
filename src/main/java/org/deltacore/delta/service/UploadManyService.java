package org.deltacore.delta.service;

import com.google.cloud.storage.transfermanager.ParallelUploadConfig;
import com.google.cloud.storage.transfermanager.TransferManager;
import com.google.cloud.storage.transfermanager.TransferManagerConfig;
import com.google.cloud.storage.transfermanager.UploadResult;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class UploadManyService {

    public void uploadManyFiles(String bucketName, List<Path> files) {
        try(TransferManager transferManager = TransferManagerConfig.newBuilder().build().getService()) {

            ParallelUploadConfig parallelUploadConfig = ParallelUploadConfig.newBuilder()
                    .setBucketName(bucketName)
                    .build();

            List<UploadResult> results = transferManager.uploadFiles(files, parallelUploadConfig).getUploadResults();

            for (UploadResult result : results) {
                System.out.println("Upload for " + result.getInput().getName() + " completed with status " + result.getStatus());
            }
        } catch (Exception e) {
            System.err.println("Error uploading files: " + e.getMessage());
        }
    }
}
