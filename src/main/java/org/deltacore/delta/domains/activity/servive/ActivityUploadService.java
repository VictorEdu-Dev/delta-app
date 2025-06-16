package org.deltacore.delta.domains.activity.servive;

import com.google.cloud.storage.*;
import org.deltacore.delta.domains.activity.rest.FileType;
import org.deltacore.delta.core.config.GcpStorageInfo;
import org.deltacore.delta.domains.activity.dto.ActivityFilesDTO;
import org.deltacore.delta.domains.activity.dto.ActivityFilesMapper;
import org.deltacore.delta.domains.activity.dto.ActivityMapper;
import org.deltacore.delta.domains.activity.exception.LargeFileException;
import org.deltacore.delta.shared.exception.ResourceNotFoundException;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.domains.activity.model.ActivityFiles;
import org.deltacore.delta.domains.activity.repository.ActivityDAO;
import org.deltacore.delta.domains.activity.repository.ActivityFilesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ActivityUploadService {
    private final String BUCKET_NAME;
    private final String FOLDER_PATH;
    private final String FOLDER_PATH_DOC;
    private final String FOLDER_PATH_IMG;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String DEMILIMETER = "@";

    private final ActivityFilesDAO activityFilesDAO;
    private final ActivityDAO activityDAO;
    private final ActivityFilesMapper activityFilesMapper;
    private final ActivityMapper activityMapper;
    private final Storage storage;

    @Autowired
    public ActivityUploadService(
            GcpStorageInfo gcpStorageInfo,
            ActivityFilesDAO activityFilesDAO,
            ActivityFilesMapper activityFilesMapper,
            ActivityDAO activityDAO,
            ActivityMapper activityMapper,
            Storage storage) {
        this.activityFilesDAO = activityFilesDAO;
        this.activityFilesMapper = activityFilesMapper;
        this.activityDAO = activityDAO;
        this.activityMapper = activityMapper;
        this.storage = storage;

        this.BUCKET_NAME = gcpStorageInfo.getBucketName();
        this.FOLDER_PATH = gcpStorageInfo.getFolderPath();
        this.FOLDER_PATH_IMG = gcpStorageInfo.getFolderPathImage();
        this.FOLDER_PATH_DOC = gcpStorageInfo.getFolderPathDoc();

    }

    public List<ActivityFilesDTO> uploadAndSaveFiles(MultipartFile[] files, Long id) throws IOException {
        Optional<Activity> activity = activityDAO.findById(id);
        if (activity.isEmpty()) throw new ResourceNotFoundException("Activity not found with id: " + id);
        List<ActivityFilesDTO> metadataList = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            Optional<ActivityFiles> activityFiles = activityFilesDAO.findByTemplate(file.getOriginalFilename(), id);
            if (activityFiles.isPresent()) continue;

            if(file.getSize() > MAX_FILE_SIZE) {
                throw new LargeFileException("File size exceeds the limit of 5 MB: " + file.getOriginalFilename());
            }

            ActivityFilesDTO dto = processAndUploadFile(file);
            ActivityFilesDTO savedDto = saveMetadata(dto, activity.get());
            savedDto = savedDto.toBuilder()
                    .activity(activityMapper.toDTO(activity.get()))
                    .build();

            metadataList.add(savedDto);
        }

        return metadataList;
    }

    private ActivityFilesDTO processAndUploadFile(MultipartFile file) throws IOException {
        String fileName = generateUniqueFileName(file);
        String objectName = getString(file, fileName);

        BlobId blobId = BlobId.of(BUCKET_NAME, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        byte[] content = file.getBytes();
        Storage.BlobTargetOption precondition = getPrecondition(this.storage, objectName);

        this.storage.create(blobInfo, content, precondition);

        return ActivityFilesDTO.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .size(file.getSize())
                .filePath(FOLDER_PATH)
                .build();
    }

    private String getString(MultipartFile file, String fileName) {
        String objectName;
        switch (file.getContentType()) {
            case FileType.IMAGE_JPEG, FileType.IMAGE_PNG ->
                objectName = FOLDER_PATH + FOLDER_PATH_IMG + fileName;

            case FileType.APPLICATION_PDF, FileType.APPLICATION_DOCX, FileType.APPLICATION_DOC ->
                objectName = FOLDER_PATH + FOLDER_PATH_DOC + fileName;

            case null -> throw new IllegalStateException("File type cannot be null: " + file.getOriginalFilename());
            default -> throw new IllegalStateException("Unexpected value: " + file.getContentType());
        }
        return objectName;
    }

    private String generateUniqueFileName(MultipartFile file) {
        return UUID.randomUUID() + DEMILIMETER + Objects.requireNonNull(file.getOriginalFilename());
    }

    private Storage.BlobTargetOption getPrecondition(Storage storage, String objectName) {
        if (storage.get(BUCKET_NAME, objectName) == null) {
            return Storage.BlobTargetOption.doesNotExist();
        } else {
            return Storage.BlobTargetOption.generationMatch(
                    storage.get(BUCKET_NAME, objectName).getGeneration());
        }
    }

    public ActivityFilesDTO saveMetadata(ActivityFilesDTO activityFilesDTO, Activity activity) {
        ActivityFiles activityFiles = activityFilesMapper.toEntity(activityFilesDTO);
        activityFiles.setCreatedAt(LocalDateTime.now());
        activityFiles.setActivity(activity);

        return activityFilesMapper.toDTO(activityFilesDAO.save(activityFiles));
    }
}
