package org.deltacore.delta.domains.profile.servive;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.deltacore.delta.core.config.GcpStorageInfo;
import org.deltacore.delta.domains.activity.rest.FileType;
import org.deltacore.delta.domains.profile.dto.ProfileDTO;
import org.deltacore.delta.shared.exception.EmptyFileException;
import org.deltacore.delta.shared.exception.InvalidFileTypeException;
import org.deltacore.delta.domains.profile.exception.ProfileImageTooLargeException;
import org.deltacore.delta.domains.profile.exception.ProfileNotFoundException;
import org.deltacore.delta.domains.profile.model.Profile;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.domains.profile.repository.ProfileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProfileImageUploadService {
    private final String BUCKET_NAME;
    private final String PROFILE_IMAGE_FOLDER_PATH;
    private static final long MAX_PROFILE_IMAGE_SIZE = 5 * 1024 * 1024;
    private static final String DELIMITER = "@";

    private final Storage storage;
    private MessageSource messageSource;

    private ProfileDAO profileDAO;

    @Autowired
    public ProfileImageUploadService(
            GcpStorageInfo gcpStorageInfo,
            Storage storage) {
        this.storage = storage;
        this.BUCKET_NAME = gcpStorageInfo.getBucketName();
        this.PROFILE_IMAGE_FOLDER_PATH = gcpStorageInfo.getFolderPathProfile();
    }

    @Transactional
    public ProfileDTO uploadProfileImage(MultipartFile file, User user) throws IOException {
        if (file.isEmpty()) {
            throw new EmptyFileException(getMessage("file.upload.empty"));
        }

        if (file.getSize() > MAX_PROFILE_IMAGE_SIZE) {
            throw new ProfileImageTooLargeException(getMessage("file.profile_image.large", file.getOriginalFilename()));
        }

        if (!FileType.IMAGE_JPEG.equals(file.getContentType()) && !FileType.IMAGE_PNG.equals(file.getContentType())) {
            throw new InvalidFileTypeException(getMessage("file.profile_image.invalid_type"));
        }

        return processAndUploadProfileImage(file, user);
    }

    private ProfileDTO processAndUploadProfileImage(MultipartFile file, User user) throws IOException {
        Profile profile = profileDAO.findById(user.getProfile().getId())
                .orElseThrow(() -> new ProfileNotFoundException(getMessage("profile.not_found", user.getId())));

        String oldImage = profile.getProfileImage();
        if (oldImage != null && !oldImage.isBlank()) {
            storage.delete(BUCKET_NAME, PROFILE_IMAGE_FOLDER_PATH + oldImage);
        }

        String newImage = generateUniqueFileName(file);
        BlobInfo blob = BlobInfo.newBuilder(BlobId.of(BUCKET_NAME, PROFILE_IMAGE_FOLDER_PATH + newImage)).build();
        storage.create(blob, file.getBytes());

        profile.setProfileImage(newImage);

        return ProfileDTO.builder()
                .name(profile.getName())
                .bio(profile.getBio())
                .profileImage(profile.getProfileImage())
                .phoneNumber(profile.getPhoneNumber())
                .level(profile.getLevel())
                .totalScore(profile.getTotalScore())
                .build();
    }

    private String generateUniqueFileName(MultipartFile file) {
        return UUID.randomUUID() + DELIMITER + Objects.requireNonNull(file.getOriginalFilename());
    }

    private Storage.BlobTargetOption getPrecondition(Storage storage, String objectName) {
        if (storage.get(BUCKET_NAME, objectName) == null) {
            return Storage.BlobTargetOption.doesNotExist();
        } else {
            return Storage.BlobTargetOption.generationMatch(
                    storage.get(BUCKET_NAME, objectName).getGeneration());
        }
    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    @Autowired @Lazy
    private void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired @Lazy
    private void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }
}
