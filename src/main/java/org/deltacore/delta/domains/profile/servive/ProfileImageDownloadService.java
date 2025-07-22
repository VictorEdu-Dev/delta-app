package org.deltacore.delta.domains.profile.servive;

import com.google.cloud.storage.*;
import org.deltacore.delta.core.config.GcpStorageInfo;
import org.deltacore.delta.domains.profile.exception.ConflictException;
import org.deltacore.delta.domains.profile.exception.ProfileNotFoundException;
import org.deltacore.delta.domains.profile.model.Profile;
import org.deltacore.delta.domains.profile.model.User;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class ProfileImageDownloadService {

    private final Storage storage;
    private final GcpStorageInfo gcpStorageInfo;
    private final MessageSource messageSource;

    public ProfileImageDownloadService(Storage storage,
                                       GcpStorageInfo gcpStorageInfo,
                                       MessageSource messageSource) {
        this.storage = storage;
        this.gcpStorageInfo = gcpStorageInfo;
        this.messageSource = messageSource;
    }

    public byte[] downloadProfileImage(String fileName, User user) {
        checkProfile(fileName, user);

        String folder = gcpStorageInfo.getFolderPathProfile();
        if (!folder.endsWith("/")) folder += "/";
        String objectName = folder + fileName;
        Blob blob = storage.get(BlobId.of(gcpStorageInfo.getBucketName(), objectName));

        if (blob == null || !blob.exists()) {
            throw new ProfileNotFoundException(getMessage("profile.image.not_found", fileName));
        }

        return blob.getContent();
    }

    public URL getSignedUrlForProfileImage(String fileName, User user) {
        checkProfile(fileName, user);

        String objectName = gcpStorageInfo.getFolderPathProfile() + fileName;
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(gcpStorageInfo.getBucketName(), objectName)).build();

        return storage.signUrl(blobInfo, 15, TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.httpMethod(HttpMethod.GET));
    }

    private void checkProfile(String fileName, User user) {
        Profile profile = user.getProfile();
        if (profile == null || profile.getId() == null)
            throw new ProfileNotFoundException(getMessage("profile.not_found", user.getId()));
        if(profile.getProfileImage() == null || profile.getProfileImage().isEmpty())
            throw new ProfileNotFoundException(getMessage("profile.image.not_found", user.getId()));
        if(!profile.getProfileImage().equals(fileName)) throw new ConflictException(getMessage("profile.image.conflict", user.getId()));
    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
