package org.deltacore.delta.domains.profile.servive;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.deltacore.delta.core.config.GcpStorageInfo;
import org.deltacore.delta.domains.activity.rest.FileType;
import org.deltacore.delta.domains.profile.dto.TutorRequestDTO;
import org.deltacore.delta.domains.profile.model.TutorRequest;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.domains.profile.repository.TutorRequestDAO;
import org.deltacore.delta.domains.tutoring.model.Subject;
import org.deltacore.delta.domains.tutoring.repository.SubjectDAO;
import org.deltacore.delta.shared.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
public class TutorRegisterService {

    private SubjectDAO subjectDAO;
    private TutorRequestDAO tutorRequestDAO;
    private Storage storage;
    private GcpStorageInfo gcpStorageInfo;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String DELIMITER = "@";
    private static final String TUTOR_DOCS_FOLDER = "prod/tutor_request/";
    private MessageSource messageSource;

    public TutorRequestDTO submitRequest(User user, String code, MultipartFile file) {
        validateRequest(user);
        validateFile(file);

        String uploadedFileName;
        try {
            uploadedFileName = processAndUploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(getMessage("tutor.error.file.upload"));
        }

        Subject subject = subjectDAO.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("error.subject.not.found")));
        TutorRequest tr = TutorRequest
                .builder()
                .user(user)
                .subject(subject)
                .filePath(uploadedFileName)
                .createdAt(OffsetDateTime.now())
                .build();
        tr = tutorRequestDAO.save(tr);

        return TutorRequestDTO
                .builder()
                .id(tr.getId())
                .user(user.getUsername())
                .subject(subject.getName())
                .createdAt(tr.getCreatedAt())
                .build();
    }

    private void validateRequest(User user) {
        Integer hasRequest = tutorRequestDAO.verifyTutorRequest(user.getId());
        if (hasRequest != null && hasRequest > 0)
            throw new ConflictException(getMessage("tutor.exists.conflict"));
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException(getMessage("tutor.empty.file"));
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileTooLargeException(getMessage("tutor.file.too.large"));
        }

        if (!FileType.APPLICATION_PDF.equals(file.getContentType()) && !FileType.APPLICATION_DOCX.equals(file.getContentType())) {
            throw new InvalidFileTypeException(getMessage("tutor.file.invalid_type"));
        }
    }

    private String processAndUploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = generateUniqueFileName(file);
        String objectName = TUTOR_DOCS_FOLDER + uniqueFileName;

        BlobId blobId = BlobId.of(gcpStorageInfo.getBucketName(), objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, file.getBytes());

        return uniqueFileName;
    }

    private String generateUniqueFileName(MultipartFile file) {
        return UUID.randomUUID() + DELIMITER + file.getOriginalFilename();
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, "Mensagem não encontrada", Locale.getDefault());
    }

    @Autowired
    public void setSubjectDAO(SubjectDAO subjectDAO) {
        this.subjectDAO = subjectDAO;
    }

    @Autowired
    public void setTutorRequestDAO(TutorRequestDAO tutorRequestDAO) {
        this.tutorRequestDAO = tutorRequestDAO;
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Autowired
    public void setGcpStorageInfo(GcpStorageInfo gcpStorageInfo) {
        this.gcpStorageInfo = gcpStorageInfo;
    }

    @Autowired
    private void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
