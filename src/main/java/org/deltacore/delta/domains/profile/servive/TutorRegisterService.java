package org.deltacore.delta.domains.profile.servive;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.deltacore.delta.core.config.GcpStorageInfo;
import org.deltacore.delta.domains.activity.rest.FileType;
import org.deltacore.delta.domains.profile.dto.TutorRequestDTO;
import org.deltacore.delta.domains.profile.exception.ConflictException;
import org.deltacore.delta.domains.profile.exception.EmptyFileException;
import org.deltacore.delta.domains.profile.exception.InvalidFileTypeException;
import org.deltacore.delta.domains.profile.exception.ProfileImageTooLargeException;
import org.deltacore.delta.domains.profile.model.TutorRequest;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.domains.profile.repository.TutorRequestDAO;
import org.deltacore.delta.domains.tutoring.model.Subject;
import org.deltacore.delta.domains.tutoring.repository.SubjectDAO;
import org.deltacore.delta.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
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

    public TutorRequestDTO submitRequest(User user, String code, MultipartFile file) {
        validateRequest(user);
        validateFile(file);

        String uploadedFileName;
        try {
            uploadedFileName = processAndUploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file for tutor request.", e);
        }

        Subject subject = subjectDAO.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with code: " + code));
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
            throw new ConflictException("Já existe um pedido de tutor cadastrado para o usuário: " + user.getUsername());
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException("O arquivo não pode estar vazio.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ProfileImageTooLargeException("O tamanho do arquivo excede o limite de 5 MB: " + file.getOriginalFilename());
        }

        if (!FileType.APPLICATION_PDF.equals(file.getContentType()) && !FileType.APPLICATION_DOCX.equals(file.getContentType())) {
            throw new InvalidFileTypeException("Tipo de arquivo inválido. Apenas PDF ou DOCX são permitidos.");
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
}
