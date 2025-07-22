package org.deltacore.delta.domains.profile.servive;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import jakarta.transaction.Transactional;
import org.deltacore.delta.core.config.GcpStorageInfo;
import org.deltacore.delta.domains.profile.exception.ProfileImageNotSetException;
import org.deltacore.delta.domains.profile.exception.ProfileNotFoundException;
import org.deltacore.delta.domains.profile.model.Profile;
import org.deltacore.delta.domains.profile.repository.ProfileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProfileDeleteService {
    private ProfileDAO profileDAO;
    private Storage storage;
    private GcpStorageInfo gcpStorageInfo;
    private MessageSource messageSource;

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    @Transactional
    public void removeProfileImage(Profile profile) {
        if (profile == null || profile.getId() == null)
            throw new ProfileNotFoundException(getMessage("profile.not_found"));

        String currentProfileImageObjectName = gcpStorageInfo.getFolderPathProfile() + profile.getProfileImage();

        if (currentProfileImageObjectName.isEmpty())
            throw new ProfileImageNotSetException(getMessage("profile.image.not.set"));

        BlobId blobId = BlobId.of(gcpStorageInfo.getBucketName(), currentProfileImageObjectName);

        storage.delete(blobId);

        profile.setProfileImage(null);
        profileDAO.save(profile);
    }

    @Autowired
    private void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Autowired
    private void setGcpStorageInfo(GcpStorageInfo gcpStorageInfo) {
        this.gcpStorageInfo = gcpStorageInfo;
    }

    @Autowired
    private void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }
}
